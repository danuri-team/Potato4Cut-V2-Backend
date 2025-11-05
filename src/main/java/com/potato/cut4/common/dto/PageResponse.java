package com.potato.cut4.common.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class PageResponse<T> {

  private final List<T> content;
  private final int currentPage;
  private final int pageSize;
  private final long totalElements;
  private final int totalPages;
  private final boolean first;
  private final boolean last;
  private final boolean hasNext;
  private final boolean hasPrevious;

  public static <T> PageResponse<T> of(Page<T> page) {
    return PageResponse.<T>builder()
        .content(page.getContent())
        .currentPage(page.getNumber())
        .pageSize(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .first(page.isFirst())
        .last(page.isLast())
        .hasNext(page.hasNext())
        .hasPrevious(page.hasPrevious())
        .build();
  }
}
