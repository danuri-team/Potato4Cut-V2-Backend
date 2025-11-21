package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.CreatorService;
import com.potato.cut4.application.service.NotificationService;
import com.potato.cut4.application.service.ReportService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.common.dto.PageResponse;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.type.FrameStatus;
import com.potato.cut4.persistence.repository.FrameRepository;
import com.potato.cut4.presentation.dto.request.CreatorReviewRequest;
import com.potato.cut4.presentation.dto.request.SendNotificationRequest;
import com.potato.cut4.presentation.dto.response.CreatorResponse;
import com.potato.cut4.presentation.dto.response.FrameListResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final CreatorService creatorService;
  private final FrameRepository frameRepository;
  private final ReportService reportService;
  private final NotificationService notificationService;

  // 크리에이터 관리
  @GetMapping("/creators/pending")
  public ResponseEntity<ApiResponse<PageResponse<CreatorResponse>>> getPendingCreators(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
    Page<CreatorResponse> creators = creatorService.getPendingCreators(pageable);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(creators)));
  }

  @PutMapping("/creators/{creatorId}/review")
  public ResponseEntity<ApiResponse<CreatorResponse>> reviewCreator(
      @PathVariable UUID creatorId,
      @RequestBody CreatorReviewRequest request) {

    CreatorResponse response;
    if (request.getApproved()) {
      response = creatorService.approveCreator(creatorId);
      return ResponseEntity.ok(ApiResponse.success(response, "크리에이터가 승인되었습니다."));
    } else {
      response = creatorService.rejectCreator(creatorId, request.getRejectionReason());
      return ResponseEntity.ok(ApiResponse.success(response, "크리에이터 신청이 반려되었습니다."));
    }
  }

  // 프레임 검수
  @GetMapping("/frames/pending")
  public ResponseEntity<ApiResponse<PageResponse<FrameListResponse>>> getPendingFrames(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Frame> frames = frameRepository.findByStatus(FrameStatus.PENDING, pageable);
    Page<FrameListResponse> responses = frames.map(FrameListResponse::from);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(responses)));
  }

  @PostMapping("/frames/{frameId}/approve")
  public ResponseEntity<ApiResponse<Void>> approveFrame(@PathVariable UUID frameId) {
    Frame frame = frameRepository.findById(frameId).orElseThrow();
    frame.approve();

    return ResponseEntity.ok(ApiResponse.successWithMessage("프레임이 승인되었습니다."));
  }

  @PostMapping("/frames/{frameId}/reject")
  public ResponseEntity<ApiResponse<Void>> rejectFrame(
      @PathVariable UUID frameId,
      @RequestParam String reason) {

    Frame frame = frameRepository.findById(frameId).orElseThrow();
    frame.reject(reason);

    return ResponseEntity.ok(ApiResponse.successWithMessage("프레임이 반려되었습니다."));
  }

  // 신고 관리
  @PostMapping("/reports/{reportId}/accept")
  public ResponseEntity<ApiResponse<Void>> acceptReport(
      @PathVariable UUID reportId,
      @RequestParam String adminNote) {

    UUID adminId = AuthenticationUtil.getCurrentUserId();
    reportService.acceptReport(adminId, reportId, adminNote);

    return ResponseEntity.ok(ApiResponse.successWithMessage("신고가 승인되었습니다."));
  }

  @PostMapping("/reports/{reportId}/reject")
  public ResponseEntity<ApiResponse<Void>> rejectReport(
      @PathVariable UUID reportId,
      @RequestParam String adminNote) {

    UUID adminId = AuthenticationUtil.getCurrentUserId();
    reportService.rejectReport(adminId, reportId, adminNote);

    return ResponseEntity.ok(ApiResponse.successWithMessage("신고가 반려되었습니다."));
  }

  // 알림 발송
  @PostMapping("/notifications/send")
  public ResponseEntity<ApiResponse<Void>> sendNotification(
      @Valid @RequestBody SendNotificationRequest request) {

    notificationService.sendNotificationToUsers(
        request.getTargetUserIds(),
        request.getType(),
        request.getContent(),
        request.getTargetUrl()
    );

    String message = (request.getTargetUserIds() == null || request.getTargetUserIds().isEmpty())
        ? "전체 사용자에게 알림을 발송했습니다."
        : request.getTargetUserIds().size() + "명의 사용자에게 알림을 발송했습니다.";

    return ResponseEntity.ok(ApiResponse.successWithMessage(message));
  }
}
