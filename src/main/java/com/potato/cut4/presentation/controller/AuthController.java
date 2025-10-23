package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.AuthService;
import com.potato.cut4.presentation.dto.request.SocialLoginRequest;
import com.potato.cut4.presentation.dto.request.TokenRefreshRequest;
import com.potato.cut4.presentation.dto.response.AuthResponse;
import com.potato.cut4.presentation.dto.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
    AuthResponse response = authService.socialLogin(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refreshToken(
      @Valid @RequestBody TokenRefreshRequest request) {
    TokenResponse response = authService.refreshToken(request.getRefreshToken());
    return ResponseEntity.ok(response);
  }
}
