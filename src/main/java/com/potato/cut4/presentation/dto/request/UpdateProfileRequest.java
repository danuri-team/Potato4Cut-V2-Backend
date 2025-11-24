package com.potato.cut4.presentation.dto.request;

import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileRequest {

  @Size(min = 2, max = 50, message = "닉네임은 2자 이상 50자 이하여야 합니다.")
  private String nickname;

  @Size(max = 500, message = "자기소개는 500자 이하여야 합니다.")
  private String bio;

  private UUID profilePresetId;
}
