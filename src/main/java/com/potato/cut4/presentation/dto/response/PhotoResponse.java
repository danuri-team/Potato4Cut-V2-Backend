package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Photo;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoResponse {

  private UUID photoId;
  private String composedImageUrl;
  private UUID frameId;
  private String frameTitle;

  public static PhotoResponse from(Photo photo) {
    return PhotoResponse.builder()
        .photoId(photo.getId())
        .composedImageUrl(photo.getComposedImageUrl())
        .frameId(photo.getFrame() != null ? photo.getFrame().getId() : null)
        .frameTitle(photo.getFrame() != null ? photo.getFrame().getTitle() : null)
        .build();
  }
}
