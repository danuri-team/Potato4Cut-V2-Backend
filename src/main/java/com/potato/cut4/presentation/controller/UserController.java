package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.UserService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.common.dto.PageResponse;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.presentation.dto.request.UpdateProfileRequest;
import com.potato.cut4.presentation.dto.response.PhotoListResponse;
import com.potato.cut4.presentation.dto.response.UserInfoResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser() {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    User user = userService.getUserById(userId);

    UserInfoResponse response = UserInfoResponse.builder()
        .userId(user.getId())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .provider(user.getSocialProvider())
        .profileImageUrl(user.getProfileImageUrl())
        .bio(user.getBio())
        .role(user.getRole())
        .build();

    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<UserInfoResponse>> updateProfile(
      @Valid @RequestPart(value = "data") UpdateProfileRequest request,
      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    User user = userService.updateProfile(userId, request, profileImage);

    UserInfoResponse response = UserInfoResponse.builder()
        .userId(user.getId())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .provider(user.getSocialProvider())
        .profileImageUrl(user.getProfileImageUrl())
        .bio(user.getBio())
        .role(user.getRole())
        .build();

    return ResponseEntity.ok(ApiResponse.success(response, "프로필이 수정되었습니다."));
  }

  @DeleteMapping("/me")
  public ResponseEntity<ApiResponse<Void>> deleteAccount() {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    userService.deleteAccount(userId);
    return ResponseEntity.ok(ApiResponse.successWithMessage("계정이 삭제되었습니다."));
  }

  @GetMapping("/me/photos")
  public ResponseEntity<ApiResponse<PageResponse<PhotoListResponse>>> getMyPhotos(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<PhotoListResponse> photos = userService.getMyPhotos(userId, pageable);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(photos)));
  }
}
