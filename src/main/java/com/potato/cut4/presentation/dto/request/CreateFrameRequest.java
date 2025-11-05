package com.potato.cut4.presentation.dto.request;

import com.potato.cut4.persistence.domain.type.FrameCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateFrameRequest {

  @NotBlank(message = "프레임 제목은 필수입니다.")
  @Size(max = 100, message = "프레임 제목은 100자 이하여야 합니다.")
  private String title;

  @Size(max = 1000, message = "프레임 설명은 1000자 이하여야 합니다.")
  private String description;

  @NotNull(message = "카테고리는 필수입니다.")
  private FrameCategory category;

  @Size(max = 10, message = "태그는 최대 10개까지 추가할 수 있습니다.")
  private List<String> tags;
}
