package com.safevillage.safevillage.domain.reports.repository;

import com.safevillage.safevillage.domain.reports.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {}
