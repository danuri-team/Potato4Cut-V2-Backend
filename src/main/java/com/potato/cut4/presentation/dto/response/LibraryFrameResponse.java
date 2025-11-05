package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.UserFrameLibrary;
import com.potato.cut4.persistence.domain.type.FrameCategory;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LibraryFrameResponse {

  private UUID frameId;
  private String title;
  private String previewImageUrl;
  private String frameImageUrl;
  private FrameCategory category;
  private boolean bookmarked;
  private LocalDateTime addedAt;
  private LocalDateTime lastUsedAt;
  private UUID creatorId;
  private String creatorNickname;

  public static LibraryFrameResponse from(UserFrameLibrary library) {
    return LibraryFrameResponse.builder()
        .frameId(library.getFrame().getId())
        .title(library.getFrame().getTitle())
        .previewImageUrl(library.getFrame().getPreviewImageUrl())
        .frameImageUrl(library.getFrame().getFrameImageUrl())
        .category(library.getFrame().getCategory())
        .bookmarked(library.isBookmarked())
        .addedAt(library.getAddedAt())
        .lastUsedAt(library.getLastUsedAt())
        .creatorId(library.getFrame().getCreator().getId())
        .creatorNickname(library.getFrame().getCreator().getUser().getNickname())
        .build();
  }
}
