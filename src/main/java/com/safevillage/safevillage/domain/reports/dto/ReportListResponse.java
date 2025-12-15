package com.safevillage.safevillage.domain.reports.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class ReportListResponse {

  // Report를 Report List형태로 변환
  private List<ReportResponse> reports;

  // 페이지네이션을 위한 페이지 정보
  private int currentPage;
  private int totalPages;
  private long totalElements;

  public static ReportListResponse from(Page<ReportResponse> page) {
    return ReportListResponse.builder()
        .reports(page.getContent())
        .currentPage(page.getNumber())
        .totalPages(page.getTotalPages())
        .totalElements(page.getTotalElements())
        .build();
  }
}
