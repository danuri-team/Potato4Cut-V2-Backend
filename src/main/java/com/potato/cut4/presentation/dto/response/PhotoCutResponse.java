package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.PhotoCut;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoCutResponse {

  private UUID cutId;
  private int cutOrder;
  private String originalImageUrl;

  public static PhotoCutResponse from(PhotoCut photoCut) {
    return PhotoCutResponse.builder()
        .cutId(photoCut.getId())
        .cutOrder(photoCut.getCutOrder())
        .originalImageUrl(photoCut.getOriginalImageUrl())
        .build();
  }
}
