package com.potato.cut4.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatorReviewRequest {

  @NotNull(message = "승인 여부는 필수입니다.")
  private Boolean approved;

  @Size(max = 1000, message = "거절 사유는 1000자 이하여야 합니다.")
  private String rejectionReason;
}
