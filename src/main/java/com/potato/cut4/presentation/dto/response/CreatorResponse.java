package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Creator;
import com.potato.cut4.persistence.domain.type.CreatorStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatorResponse {

  private UUID creatorId;
  private UUID userId;
  private String nickname;
  private String profileImageUrl;
  private CreatorStatus status;
  private String rejectionReason;
  private LocalDateTime appliedAt;
  private LocalDateTime approvedAt;
  private int totalFrames;
  private long totalDownloads;

  public static CreatorResponse from(Creator creator) {
    return CreatorResponse.builder()
        .creatorId(creator.getId())
        .userId(creator.getUser().getId())
        .nickname(creator.getUser().getNickname())
        .profileImageUrl(creator.getUser().getProfileImageUrl())
        .status(creator.getStatus())
        .rejectionReason(creator.getRejectionReason())
        .appliedAt(creator.getAppliedAt())
        .approvedAt(creator.getApprovedAt())
        .totalFrames(creator.getFrames().size())
        .totalDownloads(creator.getFrames().stream()
            .mapToLong(frame -> frame.getDownloadCount())
            .sum())
        .build();
  }
}
