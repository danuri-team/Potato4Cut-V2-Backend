package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Report;
import com.potato.cut4.persistence.domain.type.ReportStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, UUID> {

  Page<Report> findByStatus(ReportStatus status, Pageable pageable);
}
