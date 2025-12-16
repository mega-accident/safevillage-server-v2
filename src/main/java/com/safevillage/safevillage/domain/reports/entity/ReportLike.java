package com.safevillage.safevillage.domain.reports.entity;

import com.safevillage.safevillage.domain.auth.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "report_likes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_report_like_user_report",
            columnNames = {"user_id", "report_id"}
        )
    }
)
public class ReportLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "report_id", nullable = false)
  private Report report;

  @Builder
  public ReportLike(User user, Report report){
    this.user = user;
    this.report = report;
    this.createdAt = LocalDateTime.now();
  }

}
