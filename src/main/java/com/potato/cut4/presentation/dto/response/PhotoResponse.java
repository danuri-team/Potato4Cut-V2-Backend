package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.Photo;
import java.util.Optional;
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
        .frameId(Optional.ofNullable(photo.getFrame())
            .map(Frame::getId)
            .orElse(null)
        )
        .frameTitle(
            Optional.ofNullable(photo.getFrame())
                .map(Frame::getTitle)
                .orElse(null)
        )
        .build();
  }
}
