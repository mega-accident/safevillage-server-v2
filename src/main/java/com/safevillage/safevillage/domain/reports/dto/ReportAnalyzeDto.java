package com.safevillage.safevillage.domain.reports.dto;

import com.safevillage.safevillage.domain.reports.enums.DangerLevel;
import com.safevillage.safevillage.domain.reports.enums.ReportCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportAnalyzeDto {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "설명을 입력해주세요")
    private String description;

    @NotNull(message = "카테고리는 필수입니다")
    private ReportCategory reportCategory;

    @NotNull(message = "위험도는 필수 입력값입니다")
    private DangerLevel dangerLevel;

    @Setter
    @NotBlank(message = "이미지 URL은 필수입니다")
    private String image;

}
