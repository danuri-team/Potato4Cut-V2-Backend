package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.ShareService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.presentation.dto.response.PhotoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shares")
@RequiredArgsConstructor
public class ShareController {

  private final ShareService shareService;

  @GetMapping("/photo/{code}")
  public ResponseEntity<ApiResponse<PhotoResponse>> getPhoto(@PathVariable String code) {
    return ResponseEntity.ok(ApiResponse.success(shareService.getPhoto(code)));
  }
}