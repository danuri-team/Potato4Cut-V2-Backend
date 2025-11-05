package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.NotificationService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.common.dto.PageResponse;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.presentation.dto.response.NotificationResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getMyNotifications(
      @RequestParam(defaultValue = "false") boolean unreadOnly,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<NotificationResponse> notifications = notificationService.getMyNotifications(userId,
        unreadOnly, pageable);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(notifications)));
  }

  @PutMapping("/{notificationId}/read")
  public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable UUID notificationId) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    notificationService.markAsRead(userId, notificationId);

    return ResponseEntity.ok(ApiResponse.successWithMessage("알림을 읽음 처리했습니다."));
  }

  @GetMapping("/unread-count")
  public ResponseEntity<ApiResponse<Long>> getUnreadCount() {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    long count = notificationService.getUnreadCount(userId);

    return ResponseEntity.ok(ApiResponse.success(count));
  }
}
