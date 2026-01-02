package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.Photo;
import com.potato.cut4.persistence.domain.type.PhotoShareType;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoResponse {

  private UUID photoId;
  private String imageUrl;
  private UUID frameId;
  private String frameTitle;
  private LocalDateTime shareExpireAt;
  private PhotoShareType shareType;
  private String shareCode;

  public static PhotoResponse from(Photo photo) {
    return PhotoResponse.builder()
        .photoId(photo.getId())
        .imageUrl(photo.getImageUrl())
        .frameId(Optional.ofNullable(photo.getFrame())
            .map(Frame::getId)
            .orElse(null)
        )
        .frameTitle(
            Optional.ofNullable(photo.getFrame())
                .map(Frame::getTitle)
                .orElse(null)
        )
        .shareExpireAt(photo.getShare().getExpireAt())
        .shareType(photo.getShare().getType())
        .shareCode(photo.getShare().getCode())
        .build();
  }
}
