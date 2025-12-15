package com.safevillage.safevillage.domain.auth.service;

import com.safevillage.safevillage.domain.auth.dto.auth.SigninRequest;
import com.safevillage.safevillage.domain.auth.dto.auth.SigninResponse;
import com.safevillage.safevillage.domain.auth.dto.auth.SignupRequest;
import com.safevillage.safevillage.domain.auth.entity.Role;
import com.safevillage.safevillage.domain.auth.entity.User;
import com.safevillage.safevillage.domain.auth.repository.UserRepository;
import com.safevillage.safevillage.global.exception.EmailAlreadyExistsException;
import com.safevillage.safevillage.global.exception.PhoneAlreadyExistsException;
import com.safevillage.safevillage.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Transactional
  public void signup(SignupRequest request) {
    if (userRepository.existsByPhone(request.getPhone())) {
      throw new PhoneAlreadyExistsException("이미 사용 중인 전화번호입니다");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다");
    }

    User user = User.builder()
        .name(request.getName())
        .phone(request.getPhone())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

    userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public SigninResponse signin(SigninRequest request) {
    User user = userRepository.findByPhone(request.getPhone())
        .orElseThrow(() -> new IllegalArgumentException("전화번호 또는 비밀번호가 일치하지 않습니다"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("전화번호 또는 비밀번호가 일치하지 않습니다");
    }

    String accessToken = jwtUtil.generateToken(user.getPhone(),  user.getRole());

    return new SigninResponse(accessToken);
  }
}
