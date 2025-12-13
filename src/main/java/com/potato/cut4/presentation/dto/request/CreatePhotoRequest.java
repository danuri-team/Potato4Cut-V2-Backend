package com.potato.cut4.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePhotoRequest {

  private UUID frameId;

  @NotBlank(message = "이미지 키는 필수입니다.")
  private String objectKey;
}
