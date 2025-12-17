package com.safevillage.safevillage.domain.reports.service;

import com.safevillage.safevillage.domain.auth.entity.User;
import com.safevillage.safevillage.domain.auth.repository.UserRepository;
import com.safevillage.safevillage.domain.reports.dto.ReportAnalyzeDto;
import com.safevillage.safevillage.domain.reports.dto.ReportCreateRequest;
import com.safevillage.safevillage.domain.reports.dto.ReportLikeResponse;
import com.safevillage.safevillage.domain.reports.dto.ReportResponse;
import com.safevillage.safevillage.domain.reports.enums.DangerLevel;
import com.safevillage.safevillage.domain.reports.entity.Report;
import com.safevillage.safevillage.domain.reports.entity.ReportLike;
import com.safevillage.safevillage.domain.reports.enums.ReportStatus;
import com.safevillage.safevillage.domain.reports.repository.ReportLikeRepository;
import com.safevillage.safevillage.domain.reports.repository.ReportRepository;
import com.safevillage.safevillage.domain.reports.storage.CreateUrl;
import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;
  private final UserRepository userRepository;
  private final ReportLikeRepository reportLikeRepository;

  private final CreateUrl createUrl;
  private final ChatClient chatClient;

    // 신고 생성
  @Transactional
  public ReportResponse createReport(ReportCreateRequest request, String phone) {

    // 작성자 조회
    User user = userRepository.findByPhone(phone)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다") {
        });

    // Entity 생성
    Report report = Report.builder()
        .user(user)
        .image(request.getImage())
        .title(request.getTitle())
        .description(request.getDescription())
        .category(request.getCategory())
        .dangerLevel(request.getDangerLevel())
        .status(ReportStatus.PENDING)
        .latitude(Double.parseDouble(request.getLat()))
        .longitude(Double.parseDouble(request.getLon()))
        .build();

    // DB 저장
    Report savedReport = reportRepository.save(report);

    // DTO 변환
    return toDto(savedReport);
  }

  // 위험도 최상일 시 isDanger true
  private boolean isDangerous(DangerLevel level) {
    return level == DangerLevel.CRITICAL;
  }

  @Transactional(readOnly = true)
  public Page<ReportResponse> getAllReports(Pageable pageable) {
    return reportRepository.findAll(pageable)
        .map(this::toDto);
  }

  private ReportResponse toDto(Report report) {
    String lat = null;
    String lon = null;

    if (report.getLocation() != null) {
      lat = String.valueOf(report.getLocation().getY());
      lon = String.valueOf(report.getLocation().getX());
    }

    return ReportResponse.builder()
        .id(report.getId())
        .image(report.getImage())
        .title(report.getTitle())
        .category(report.getCategory())
        .description(report.getDescription())
        .lat(lat)
        .lon(lon)
        .dangerLevel(report.getDangerLevel())
        .likeCount(report.getLikeCount())
        .isDanger(isDangerous(report.getDangerLevel()))
        .status(report.getStatus())
        .createdAt(report.getCreatedAt())
        .build();
  }

  @Transactional(readOnly = true)
  public ReportResponse getReport(Long reportId) {
    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 신고를 찾을 수 없습니다."));

    return toDto(report);
  }

  @Transactional
  public ReportLikeResponse likeReport(Long reportId, String phone) {

    User user = getUserByPhone(phone);
    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 신고를 찾을 수 없습니다."));

    if (reportLikeRepository.existsByUserAndReport(user, report)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 공감한 신고입니다.");
    }

    ReportLike reportLike = ReportLike.builder()
            .user(user)
            .report(report)
            .build();
    reportLikeRepository.save(reportLike);

    report.increaseLikeCount();

    return ReportLikeResponse.builder()
        .likeCount(report.getLikeCount())
        .build();
  }

  @Transactional
  public ReportLikeResponse unlikeReport(Long reportId, String phone) {

    User user = getUserByPhone(phone);
    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 신고를 찾을 수 없습니다."));

    ReportLike reportLike = reportLikeRepository.findByUserAndReport(user, report)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "공감하지 않은 신고입니다."));

    reportLikeRepository.delete(reportLike);

    report.decreaseLikeCount();

    return ReportLikeResponse.builder()
        .likeCount(report.getLikeCount())
        .build();
  }

  private User getUserByPhone (String phone) {
    return userRepository.findByPhone(phone)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
  }

  @SneakyThrows
  public ReportAnalyzeDto analyzeReport(MultipartFile file) {
      URL url = createUrl.uploadFile(file);
      String  prompt =
              """
              당신은 안전한 마을을 만드는 AI 비서입니다.
              이미지를 분석하여 마을 주민의 자세한 안전 신고 정보를 작성해주세요.
              - title 의 내용을 작성하세요
              - category 는 주어진 Enum 중 하나를 선택하세요
              - description 의 내용을 작성하세요
              - dangerDegree 는 주어진 Enum 중 하나를 선택하세요
              - image 는 Media 로 주어진 이미지 URL 을 그대로 작성하여 반환하세요

              *아무런 내용 없이 오직 주어진 Entity 형식의 내용만 반환하세요*
              """;

      ReportAnalyzeDto reportAnalyzeDto = chatClient.prompt()
              .user(userSpec -> userSpec.text(prompt)
                      .media(MimeTypeUtils.IMAGE_JPEG, url))
              .call()
              .entity(new ParameterizedTypeReference<ReportAnalyzeDto>() {});

      reportAnalyzeDto.setImage(url.toString());

      return reportAnalyzeDto;
  }

}
