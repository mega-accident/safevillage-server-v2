package com.safevillage.safevillage.domain.reports.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportCategory {
  FACILITY("시설물 문제"),
  TRAFFIC("보행 및 교통"),
  ENVIRONMENT("환경 문제"),
  CRIME("범죄 및 치안"),
  ETC("기타");

  private final String label;
}
