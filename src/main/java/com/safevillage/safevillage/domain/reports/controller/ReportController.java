package com.safevillage.safevillage.domain.reports.controller;

import com.safevillage.safevillage.domain.reports.dto.ReportCreateRequest;
import com.safevillage.safevillage.domain.reports.dto.ReportLikeResponse;
import com.safevillage.safevillage.domain.reports.dto.ReportListResponse;
import com.safevillage.safevillage.domain.reports.dto.ReportResponse;
import com.safevillage.safevillage.domain.reports.service.ReportService;
import com.safevillage.safevillage.global.dto.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
