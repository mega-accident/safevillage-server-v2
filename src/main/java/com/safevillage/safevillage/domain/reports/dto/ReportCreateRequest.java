package com.safevillage.safevillage.domain.reports.dto;

import com.safevillage.safevillage.domain.reports.entity.DangerLevel;
import com.safevillage.safevillage.domain.reports.entity.ReportCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCreateRequest {

  @NotBlank(message = "이미지 링크를 첨부해주세요")
  private String image;

  @NotNull(message = "카테고리는 필수 입력값입니다")
  private ReportCategory category;

  @NotBlank(message = "제목을 입력해주세요")
  private String title;

  @NotBlank(message = "설명을 입력해주세요")
  private String description;

  @NotBlank(message = "위도(lat)는 필수 입력값입니다")
  private String lat;

  @NotBlank(message = "경도(lon)는 필수 입력값입니다")
  private String lon;

  @NotNull(message = "위험도는 필수 입력값입니다")
  private DangerLevel dangerLevel;
}
