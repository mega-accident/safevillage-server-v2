package com.safevillage.safevillage.domain.auth.controller;

import com.safevillage.safevillage.domain.auth.dto.auth.SigninRequest;
import com.safevillage.safevillage.domain.auth.dto.auth.SigninResponse;
import com.safevillage.safevillage.domain.auth.dto.auth.SignupRequest;
import com.safevillage.safevillage.domain.auth.dto.auth.UserResponse;
import com.safevillage.safevillage.domain.auth.service.AuthService;
import com.safevillage.safevillage.global.dto.BaseResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private static final int ONE_DAY = 24 * 60 * 60;
  private final AuthService authService;

  @Value("${jwt.cookie.secure}")
  private boolean cookieSecure;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<Void> signup(@Valid @RequestBody SignupRequest request) {
    authService.signup(request);
    return BaseResponse.ok();
  }

  @PostMapping("/signin")
  public BaseResponse<SigninResponse> signin(@Valid @RequestBody SigninRequest request, HttpServletResponse response) {
    SigninResponse signinResponse = authService.signin(request);

    Cookie cookie = new Cookie("accessToken", signinResponse.getAccessToken());
    cookie.setHttpOnly(true);
    cookie.setSecure(cookieSecure);
    cookie.setPath("/");
    cookie.setMaxAge(ONE_DAY);

    response.addCookie(cookie);

    return BaseResponse.ok(signinResponse);
  }

  @GetMapping("/myinfo")
  public BaseResponse<UserResponse> getMyInfo(
      @AuthenticationPrincipal String phone // 토큰에서 전화번호 자동 추출
  ) {

    UserResponse response = authService.getUser(phone);

    return BaseResponse.ok(response);
  }
}
