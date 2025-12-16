package com.safevillage.safevillage.domain.reports.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportLikeResponse {
  private final Integer likeCount;
}
