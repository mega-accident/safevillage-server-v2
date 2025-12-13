package com.safevillage.safevillage.domain.auth.controller;

import com.safevillage.safevillage.domain.auth.dto.auth.SigninRequest;
import com.safevillage.safevillage.domain.auth.dto.auth.SigninResponse;
import com.safevillage.safevillage.domain.auth.dto.auth.SignupRequest;
import com.safevillage.safevillage.domain.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
    authService.signup(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/signin")
  public ResponseEntity<SigninResponse> signin(@Valid @RequestBody SigninRequest request, HttpServletResponse response) {
    SigninResponse signinResponse = authService.signin(request);

    Cookie cookie = new Cookie("accessToken", signinResponse.getAccessToken());
    cookie.setHttpOnly(true);
    cookie.setSecure(false); // 개발 환경이므로 false, 프로덕션에서는 true
    cookie.setPath("/");
    cookie.setMaxAge(24 * 60 * 60); // 24시간

    response.addCookie(cookie);

    return ResponseEntity.ok(signinResponse);
  }
}
