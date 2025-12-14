package com.safevillage.safevillage.domain.auth.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SigninRequest {

  @NotBlank(message = "전화번호는 필수입니다")
  private String phone;

  @NotBlank(message = "비밀번호는 필수입니다")
  private String password;
}
