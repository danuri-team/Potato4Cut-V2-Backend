package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.ReportService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.presentation.dto.request.CreateReportRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createReport(
      @Valid @RequestBody CreateReportRequest request) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    reportService.createReport(userId, request.getType(), request.getTargetId(),
        request.getReason());

    return ResponseEntity.ok(ApiResponse.successWithMessage("신고가 접수되었습니다."));
  }
}
