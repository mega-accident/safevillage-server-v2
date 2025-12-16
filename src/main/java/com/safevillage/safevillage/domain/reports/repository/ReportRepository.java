package com.safevillage.safevillage.domain.reports.repository;

import com.safevillage.safevillage.domain.auth.entity.User;
import com.safevillage.safevillage.domain.reports.entity.Report;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
  List<Report> findAllByUser(User user);
}
