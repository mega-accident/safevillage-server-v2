package com.safevillage.safevillage.domain.reports.repository;

import com.safevillage.safevillage.domain.auth.entity.User;
import com.safevillage.safevillage.domain.reports.entity.Report;
import com.safevillage.safevillage.domain.reports.entity.ReportLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportLikeRepository extends JpaRepository<ReportLike, Long> {

  // 이미 좋아요를 눌렀는지 확인
  boolean existsByUserAndReport(User user, Report report);

  // 좋아요 취소를 위한 데이터 조회
  Optional<ReportLike> findByUserAndReport(User user, Report report);
}
