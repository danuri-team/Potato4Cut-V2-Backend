package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.type.FrameCategory;
import com.potato.cut4.persistence.domain.type.FrameStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FrameListResponse {

  private UUID frameId;
  private String title;
  private String previewImageUrl;
  private FrameCategory category;
  private FrameStatus status;
  private long downloadCount;
  private long likeCount;
  private long viewCount;
  private UUID creatorId;
  private String creatorNickname;
  private LocalDateTime createdAt;

  public static FrameListResponse from(Frame frame) {
    return FrameListResponse.builder()
        .frameId(frame.getId())
        .title(frame.getTitle())
        .previewImageUrl(frame.getPreviewImageUrl())
        .category(frame.getCategory())
        .status(frame.getStatus())
        .downloadCount(frame.getDownloadCount())
        .likeCount(frame.getLikeCount())
        .viewCount(frame.getViewCount())
        .creatorId(frame.getCreator().getId())
        .creatorNickname(frame.getCreator().getUser().getNickname())
        .createdAt(frame.getCreatedAt())
        .build();
  }
}
