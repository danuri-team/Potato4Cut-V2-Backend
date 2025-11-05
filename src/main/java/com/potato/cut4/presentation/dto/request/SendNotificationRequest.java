package com.potato.cut4.presentation.dto.request;

import com.potato.cut4.persistence.domain.type.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendNotificationRequest {

  @NotNull(message = "알림 타입은 필수입니다.")
  private NotificationType type;

  @NotBlank(message = "알림 내용은 필수입니다.")
  @Size(max = 500, message = "알림 내용은 500자 이하여야 합니다.")
  private String content;

  @Size(max = 500, message = "타겟 URL은 500자 이하여야 합니다.")
  private String targetUrl;

  private List<UUID> targetUserIds;
}
