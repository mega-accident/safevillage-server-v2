package com.safevillage.safevillage.domain.reports.dto;

import com.safevillage.safevillage.domain.reports.entity.DangerLevel;
import com.safevillage.safevillage.domain.reports.entity.ReportCategory;
import com.safevillage.safevillage.domain.reports.entity.ReportStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {

  private Long id;

  private String image;

  private ReportCategory category;

  private String title;

  private String description;

  private String lat;

  private String lon;

  private DangerLevel dangerLevel;

  private Integer likeCount;

  private Boolean isDanger;

  private ReportStatus status;

  private LocalDateTime createdAt;
}
