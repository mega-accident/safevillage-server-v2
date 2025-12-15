package com.safevillage.safevillage.domain.reports.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportStatus {
  PENDING("대기"),
  RECEIVED("접수됨"),
  PROCESSING("처리중"),
  PROCESSED("처리완료");

  private final String label;
}
