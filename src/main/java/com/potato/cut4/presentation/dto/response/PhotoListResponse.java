package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Photo;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoListResponse {

  private UUID photoId;
  private String composedImageUrl;
  private UUID frameId;
  private String frameTitle;
  private LocalDateTime createdAt;

  public static PhotoListResponse from(Photo photo) {
    return PhotoListResponse.builder()
        .photoId(photo.getId())
        .composedImageUrl(photo.getComposedImageUrl())
        .frameId(photo.getFrame() != null ? photo.getFrame().getId() : null)
        .frameTitle(photo.getFrame() != null ? photo.getFrame().getTitle() : null)
        .createdAt(photo.getCreatedAt())
        .build();
  }
}
