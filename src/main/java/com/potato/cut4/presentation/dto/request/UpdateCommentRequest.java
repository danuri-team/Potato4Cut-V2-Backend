package com.potato.cut4.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCommentRequest {

  @NotBlank(message = "댓글 내용은 필수입니다.")
  @Size(max = 1000, message = "댓글은 1000자 이하여야 합니다.")
  private String content;
}
