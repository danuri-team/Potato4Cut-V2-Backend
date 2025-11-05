package com.potato.cut4.presentation.dto.request;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePhotoRequest {

  private UUID frameId; // nullable
}
