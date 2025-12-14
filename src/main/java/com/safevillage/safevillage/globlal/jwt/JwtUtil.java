package com.safevillage.safevillage.globlal.jwt;

import com.safevillage.safevillage.domain.auth.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private Long expiration;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String phone, Role role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .subject(phone)
        .claim("role", role)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public String getPhoneFromToken(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.warn("만료된 JWT 토큰입니다: {}", e.getMessage());
      return false;
    } catch (MalformedJwtException e) {
      log.warn("잘못된 형식의 JWT 토큰입니다: {}", e.getMessage());
      return false;
    } catch (SignatureException e) {
      log.warn("JWT 서명 검증에 실패했습니다: {}", e.getMessage());
      return false;
    } catch (UnsupportedJwtException e) {
      log.warn("지원하지 않는 JWT 토큰입니다: {}", e.getMessage());
      return false;
    } catch (IllegalArgumentException e) {
      log.warn("JWT 토큰이 비어있습니다: {}", e.getMessage());
      return false;
    }
  }

    public Role getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String roleString = claims.get("role", String.class);
        return Role.valueOf(roleString);
    }
}
