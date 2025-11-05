package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.FrameComment;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {

  private UUID commentId;
  private String content;
  private UUID userId;
  private String userNickname;
  private String userProfileImageUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean isOwner;

  public static CommentResponse from(FrameComment comment, UUID currentUserId) {
    boolean isOwner = currentUserId != null && comment.getUser().getId().equals(currentUserId);

    return CommentResponse.builder()
        .commentId(comment.getId())
        .content(comment.getContent())
        .userId(comment.getUser().getId())
        .userNickname(comment.getUser().getNickname())
        .userProfileImageUrl(comment.getUser().getProfileImageUrl())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .isOwner(isOwner)
        .build();
  }
}
