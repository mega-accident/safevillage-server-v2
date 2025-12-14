package com.safevillage.safevillage.domain.auth.controller;

import com.safevillage.safevillage.domain.auth.dto.auth.SigninRequest;
import com.safevillage.safevillage.domain.auth.dto.auth.SigninResponse;
import com.safevillage.safevillage.domain.auth.dto.auth.SignupRequest;
import com.safevillage.safevillage.domain.auth.service.AuthService;
import com.safevillage.safevillage.globlal.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private static final int ONE_DAY = 24 * 60 * 60;
  private final AuthService authService;

  @Value("${jwt.cookie.secure}")
  private boolean cookieSecure;

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
    cookie.setSecure(cookieSecure);
    cookie.setPath("/");
    cookie.setMaxAge(ONE_DAY);

    response.addCookie(cookie);

    return ResponseEntity.ok(signinResponse);
  }
}
