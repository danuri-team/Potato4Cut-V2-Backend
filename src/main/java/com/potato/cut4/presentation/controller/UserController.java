package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.AuthService;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.presentation.dto.response.UserInfoResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final AuthService authService;

  @GetMapping("/me")
  public ResponseEntity<UserInfoResponse> getCurrentUser() {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    User user = authService.getUserById(userId);

    UserInfoResponse response = UserInfoResponse.builder()
        .userId(user.getId())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .profileImageUrl(user.getProfileImageUrl())
        .bio(user.getBio())
        .role(user.getRole())
        .build();

    return ResponseEntity.ok(response);
  }
}
