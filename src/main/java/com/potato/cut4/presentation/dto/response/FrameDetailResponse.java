package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.FrameTag;
import com.potato.cut4.persistence.domain.Tag;
import com.potato.cut4.persistence.domain.type.FrameCategory;
import com.potato.cut4.persistence.domain.type.FrameStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FrameDetailResponse {

  private UUID frameId;
  private String title;
  private String description;
  private String frameImageUrl;
  private String previewImageUrl;
  private FrameCategory category;
  private FrameStatus status;
  private long downloadCount;
  private long likeCount;
  private long viewCount;
  private UUID creatorId;
  private String creatorNickname;
  private String creatorProfileImageUrl;
  private List<String> tags;
  private boolean isLiked;
  private boolean isInLibrary;
  private LocalDateTime createdAt;
  private LocalDateTime approvedAt;

  public static FrameDetailResponse from(Frame frame, boolean isLiked, boolean isInLibrary) {
    return FrameDetailResponse.builder()
        .frameId(frame.getId())
        .title(frame.getTitle())
        .description(frame.getDescription())
        .frameImageUrl(frame.getFrameImageUrl())
        .previewImageUrl(frame.getPreviewImageUrl())
        .category(frame.getCategory())
        .status(frame.getStatus())
        .downloadCount(frame.getDownloadCount())
        .likeCount(frame.getLikeCount())
        .viewCount(frame.getViewCount())
        .creatorId(frame.getCreator().getId())
        .creatorNickname(frame.getCreator().getUser().getNickname())
        .creatorProfileImageUrl(frame.getCreator().getUser().getProfileImageUrl())
        .tags(frame.getFrameTags().stream()
            .map(FrameTag::getTag)
            .map(Tag::getName)
            .toList())
        .isLiked(isLiked)
        .isInLibrary(isInLibrary)
        .createdAt(frame.getCreatedAt())
        .approvedAt(frame.getApprovedAt())
        .build();
  }
}
