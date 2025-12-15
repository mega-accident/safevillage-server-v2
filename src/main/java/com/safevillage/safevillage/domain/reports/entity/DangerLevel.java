package com.safevillage.safevillage.domain.reports.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DangerLevel {
  LOW("하"),
  MEDIUM("중"),
  HIGH("상"),
  CRITICAL("최상");

  private final String label;
}
