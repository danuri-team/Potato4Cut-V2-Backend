package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.common.service.FcmService;
import com.potato.cut4.persistence.domain.Notification;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.type.NotificationType;
import com.potato.cut4.persistence.repository.NotificationRepository;
import com.potato.cut4.persistence.repository.UserDeviceRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import com.potato.cut4.presentation.dto.response.NotificationResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final UserDeviceRepository userDeviceRepository;
  private final FcmService fcmService;

  @Transactional
  @Async
  public void sendNotificationToUsers(List<UUID> targetUserIds, NotificationType type,
      String content, String targetUrl) {

    List<UUID> userIds;
    List<String> fcmTokens;

    if (targetUserIds == null || targetUserIds.isEmpty()) {
      userIds = userRepository.findAllActiveAndNotificationEnabledUserIds();
      fcmTokens = userDeviceRepository.findFcmTokens();
      log.info("Sending notification to all users: count={}", userIds.size());
    } else {
      userIds = targetUserIds;
      fcmTokens = userDeviceRepository.findFcmTokensByUserIds(targetUserIds);
      log.info("Sending notification to specific users: count={}", userIds.size());
    }

    List<Notification> notifications = new ArrayList<>();
    for (UUID userId : userIds) {
      User user = userRepository.getReferenceById(userId);
      Notification notification = Notification.builder()
          .user(user)
          .actor(null)
          .type(type)
          .content(content)
          .targetUrl(targetUrl)
          .build();
      notifications.add(notification);
    }
    notificationRepository.saveAll(notifications);

    if (!fcmTokens.isEmpty()) {
      List<String> failedToken = fcmService.sendMulticastNotification(fcmTokens, "4컷", content,
          null);

      userDeviceRepository.deleteAllByTokenIn(failedToken);
    }

    log.info("Notifications sent successfully: type={}, recipients={}, fcmTokens={}",
        type, userIds.size(), fcmTokens.size());
  }

  public Page<NotificationResponse> getMyNotifications(UUID userId, boolean unreadOnly,
      Pageable pageable) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Page<Notification> notifications;
    if (unreadOnly) {
      notifications = notificationRepository.findByUserAndIsReadFalse(user, pageable);
    } else {
      notifications = notificationRepository.findByUser(user, pageable);
    }

    return notifications.map(NotificationResponse::from);
  }

  @Transactional
  public void markAsRead(UUID userId, UUID notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

    if (!notification.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.FORBIDDEN);
    }

    notification.markAsRead();
  }

  public long getUnreadCount(UUID userId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return notificationRepository.countByUserAndIsReadFalse(user);
  }

  public void updateNotificationEnabled(UUID userId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.updateNotificationEnabled();
  }

}
