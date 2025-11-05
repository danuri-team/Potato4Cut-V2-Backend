package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.TagService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.presentation.dto.response.TagResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;

  @GetMapping("/popular")
  public ResponseEntity<ApiResponse<List<TagResponse>>> getPopularTags() {
    List<TagResponse> tags = tagService.getPopularTags().stream()
        .map(TagResponse::from)
        .toList();

    return ResponseEntity.ok(ApiResponse.success(tags));
  }
}
