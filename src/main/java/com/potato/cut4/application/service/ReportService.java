package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.persistence.domain.Report;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.type.ReportStatus;
import com.potato.cut4.persistence.domain.type.ReportType;
import com.potato.cut4.persistence.repository.ReportRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

  private final ReportRepository reportRepository;
  private final UserRepository userRepository;

  @Transactional
  public void createReport(UUID reporterId, ReportType type, UUID targetId, String reason) {
    User reporter = userRepository.findByIdAndDeletedFalse(reporterId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Report report = Report.builder()
        .reporter(reporter)
        .type(type)
        .targetId(targetId)
        .reason(reason)
        .status(ReportStatus.PENDING)
        .build();

    reportRepository.save(report);

    log.info("Report created: reporterId={}, type={}, targetId={}", reporterId, type, targetId);
  }

  @Transactional
  public void acceptReport(UUID adminId, UUID reportId, String adminNote) {
    User admin = userRepository.findByIdAndDeletedFalse(adminId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

    if (report.getStatus() != ReportStatus.PENDING) {
      throw new CustomException(ErrorCode.REPORT_ALREADY_PROCESSED);
    }

    report.accept(admin, adminNote);

    log.info("Report accepted: reportId={}, adminId={}", reportId, adminId);
  }

  @Transactional
  public void rejectReport(UUID adminId, UUID reportId, String adminNote) {
    User admin = userRepository.findByIdAndDeletedFalse(adminId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

    if (report.getStatus() != ReportStatus.PENDING) {
      throw new CustomException(ErrorCode.REPORT_ALREADY_PROCESSED);
    }

    report.reject(admin, adminNote);

    log.info("Report rejected: reportId={}, adminId={}", reportId, adminId);
  }
}
