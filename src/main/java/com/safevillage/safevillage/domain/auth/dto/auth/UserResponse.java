package com.safevillage.safevillage.domain.auth.dto.auth;

import com.safevillage.safevillage.domain.reports.dto.ReportResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {

  private final String name;

  private final String phone;

  private List<ReportResponse> reports;

}
