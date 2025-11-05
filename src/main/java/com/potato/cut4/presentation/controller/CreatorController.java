package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.CreatorService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.presentation.dto.response.CreatorResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/creators")
@RequiredArgsConstructor
public class CreatorController {

  private final CreatorService creatorService;

  @PostMapping("/apply")
  public ResponseEntity<ApiResponse<Void>> applyForCreator() {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    creatorService.applyForCreator(userId);
    return ResponseEntity.ok(ApiResponse.successWithMessage("크리에이터 신청에 성공했어요."));
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<CreatorResponse>> getMyCreatorInfo() {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    CreatorResponse response = creatorService.getCreatorInfo(userId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
