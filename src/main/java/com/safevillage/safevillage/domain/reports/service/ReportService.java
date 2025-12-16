package com.safevillage.safevillage.domain.reports.service;

import com.safevillage.safevillage.domain.auth.entity.User;
import com.safevillage.safevillage.domain.auth.repository.UserRepository;
import com.safevillage.safevillage.domain.reports.dto.ReportCreateRequest;
import com.safevillage.safevillage.domain.reports.dto.ReportLikeResponse;
import com.safevillage.safevillage.domain.reports.dto.ReportResponse;
import com.safevillage.safevillage.domain.reports.entity.DangerLevel;
import com.safevillage.safevillage.domain.reports.entity.Report;
import com.safevillage.safevillage.domain.reports.entity.ReportStatus;
import com.safevillage.safevillage.domain.reports.repository.ReportRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;
  private final UserRepository userRepository;

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
  public ReportLikeResponse likeReport(Long reportId) {
    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 신고를 찾을 수 없습니다."));

    report.increaseLikeCount();

    return ReportLikeResponse.builder()
        .likeCount(report.getLikeCount())
        .build();
  }

  @Transactional
  public ReportLikeResponse unlikeReport(Long reportId) {
    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 신고를 찾을 수 없습니다."));

    report.decreaseLikeCount();

    return ReportLikeResponse.builder()
        .likeCount(report.getLikeCount())
        .build();
  }
}
