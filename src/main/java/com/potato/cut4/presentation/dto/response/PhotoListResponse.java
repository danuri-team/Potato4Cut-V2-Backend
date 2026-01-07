package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.Photo;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoListResponse {

  private UUID photoId;
  private String imageUrl;
  private UUID frameId;
  private String frameTitle;
  private LocalDateTime createdAt;

  public static PhotoListResponse from(Photo photo) {
    return PhotoListResponse.builder()
        .photoId(photo.getId())
        .imageUrl(photo.getImageUrl())
        .frameId(photo.getFrame() != null ? photo.getFrame().getId() : null)
        .frameTitle(
            Optional.ofNullable(photo.getFrame())
                .map(Frame::getTitle)
                .orElse(null)
        )
        .createdAt(photo.getCreatedAt())
        .build();
  }
}
