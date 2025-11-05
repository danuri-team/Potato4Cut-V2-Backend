package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Notification;
import com.potato.cut4.persistence.domain.type.NotificationType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {

  private UUID notificationId;
  private NotificationType type;
  private String content;
  private String targetUrl;
  private boolean isRead;
  private LocalDateTime createdAt;
  private LocalDateTime readAt;
  private UUID actorId;
  private String actorNickname;
  private String actorProfileImageUrl;

  public static NotificationResponse from(Notification notification) {
    return NotificationResponse.builder()
        .notificationId(notification.getId())
        .type(notification.getType())
        .content(notification.getContent())
        .targetUrl(notification.getTargetUrl())
        .isRead(notification.isRead())
        .createdAt(notification.getCreatedAt())
        .readAt(notification.getReadAt())
        .actorId(notification.getActor() != null ? notification.getActor().getId() : null)
        .actorNickname(
            notification.getActor() != null ? notification.getActor().getNickname() : null)
        .actorProfileImageUrl(
            notification.getActor() != null ? notification.getActor().getProfileImageUrl() : null)
        .build();
  }
}
