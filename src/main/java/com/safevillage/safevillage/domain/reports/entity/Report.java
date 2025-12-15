package com.safevillage.safevillage.domain.reports.entity;

import com.safevillage.safevillage.domain.auth.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reports")
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // S3 구현 전 까지 임시 String
  @Column(columnDefinition = "text")
  private String image;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportCategory category;

  @Column(nullable = false, columnDefinition = "text")
  private String description;

  // 위경도를 합친 공간 데이터 (SRID 4326은 GPS)
  // columnDefinition은 타입 강제 결정
  @Column(columnDefinition = "geometry(Point, 4326)")
  private Point location;

  @Enumerated(EnumType.STRING)
  private DangerLevel dangerLevel;

  private Integer likeCount = 0;

  @Enumerated(EnumType.STRING)
  private ReportStatus status = ReportStatus.PENDING;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  // 신고 리스트 반환 시 모든 유저의 정보를 가져오는 문제가 발생할 수 있기 때문에 LAZY 사용
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  // Builder를 생성자에 사용하여 편리하게 사용
  @Builder
  public Report(String image, String title, ReportCategory category, String description, Double latitude, Double longitude, DangerLevel dangerLevel, ReportStatus status, User user) {
    this.image = image;
    this.title = title;
    this.category = category;
    this.description = description;
    this.location = createPoint(latitude, longitude);
    this.dangerLevel = dangerLevel;
    this.status = status != null ? status : ReportStatus.PENDING;
    this.user = user;
  }

  // 위경도 값을 Point로 바꾸는 메서드
  public static Point createPoint(Double latitude, Double longitude) {
    try {
      String pointWKT = String.format("POINT(%s %s)", longitude, latitude);
      Point point = (Point) new WKTReader().read(pointWKT);
      point.setSRID(4326);
      return point;
    } catch (ParseException e) {
      throw new RuntimeException("좌표 변환 실패", e);
    }
  }
}
