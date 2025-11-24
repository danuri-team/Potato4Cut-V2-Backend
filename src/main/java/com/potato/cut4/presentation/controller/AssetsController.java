package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.AssetsService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.persistence.domain.ProfilePreset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetsController {

  private final AssetsService assetsService;

  @GetMapping("profile")
  public ResponseEntity<ApiResponse<List<ProfilePreset>>> getProfilePresets(
  ) {
    return ResponseEntity.ok(
        ApiResponse.success(assetsService.getProfilePresets()));
  }
}
