package com.safevillage.safevillage.domain.reports.controller;

import com.safevillage.safevillage.domain.reports.dto.*;
import com.safevillage.safevillage.domain.reports.service.ReportService;
import com.safevillage.safevillage.global.dto.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  // 신고 생성
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<ReportResponse> createReport(
      @AuthenticationPrincipal String phone,
      @Valid @RequestBody ReportCreateRequest request
  ) {

    if (phone == null) {
      throw new AuthenticationException("로그인이 필요합니다.") {
      };
    }

    ReportResponse response = reportService.createReport(request, phone);

    return BaseResponse.ok(response);
  }

  // 모든 신고 조회
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse<ReportListResponse> getAllReports(
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
  ) {

    Page<ReportResponse> page = reportService.getAllReports(pageable);

    ReportListResponse response = ReportListResponse.from(page);

    return BaseResponse.ok(response);
  }

  // 신고 세부 조회
  @GetMapping("/{reportId}")
  public BaseResponse<ReportResponse> getReport(@PathVariable Long reportId) {
    ReportResponse response = reportService.getReport(reportId);
    return BaseResponse.ok(response);
  }

  // 신고 공감 추가
  @PostMapping("/{reportId}/like")
  @PreAuthorize("isAuthenticated()")
  public BaseResponse<ReportLikeResponse> likeReport(@PathVariable Long reportId, @AuthenticationPrincipal String phone) {

    ReportLikeResponse response = reportService.likeReport(reportId, phone);
    return BaseResponse.ok(response);
  }

  @DeleteMapping("/{reportId}/like")
  @PreAuthorize("isAuthenticated()")
  public BaseResponse<ReportLikeResponse> unlikeReport(@PathVariable Long reportId, @AuthenticationPrincipal String phone) {

    ReportLikeResponse response = reportService.unlikeReport(reportId, phone);
    return BaseResponse.ok(response);
  }

  // 신고 사진 AI 분석
  @SneakyThrows
  @ResponseBody
  @PostMapping("/ai-analyze")
  @PreAuthorize("isAuthenticated()")
  public ReportAnalyzeDto analyzeReport(@RequestPart("file") MultipartFile file) {
      return  reportService.analyzeReport(file);
  }
}
