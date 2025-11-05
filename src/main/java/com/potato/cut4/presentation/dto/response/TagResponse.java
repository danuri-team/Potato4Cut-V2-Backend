package com.potato.cut4.presentation.dto.response;

import com.potato.cut4.persistence.domain.Tag;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagResponse {

  private UUID tagId;
  private String name;
  private long usageCount;

  public static TagResponse from(Tag tag) {
    return TagResponse.builder()
        .tagId(tag.getId())
        .name(tag.getName())
        .usageCount(tag.getUsageCount())
        .build();
  }
}
