package com.potato.cut4.presentation.dto.request;

import com.potato.cut4.persistence.domain.type.PhotoShareType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateShareRequest {

  @NotNull(message = "공유 타입은 Null 일 수 없어요.")
  private PhotoShareType photoShareType;

  @Min(1)
  @Max(10080)
  private int expireAt;


}
