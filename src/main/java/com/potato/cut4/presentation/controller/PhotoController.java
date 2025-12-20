package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.PhotoService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.presentation.dto.request.CreatePhotoRequest;
import com.potato.cut4.presentation.dto.request.CreatePreSignedUrl;
import com.potato.cut4.presentation.dto.response.PhotoResponse;
import com.potato.cut4.presentation.dto.response.PreSignedUrlResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
public class PhotoController {

  private final PhotoService photoService;

  @PostMapping("/presigned-url")
  public ResponseEntity<ApiResponse<PreSignedUrlResponse>> generatePreSignedUrl(
      @Valid @RequestBody CreatePreSignedUrl request
  ) {
    PreSignedUrlResponse response = photoService.generatePreSignedUrl(request);

    return ResponseEntity.ok(ApiResponse.success(response, "PreSigned URL이 생성되었습니다."));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<PhotoResponse>> savePhoto(
      @Valid @RequestBody CreatePhotoRequest request
  ) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    PhotoResponse response = photoService.savePhoto(userId, request);

    return ResponseEntity.ok(ApiResponse.success(response, "4컷 사진이 저장되었습니다."));
  }

  @GetMapping("/{photoId}")
  public ResponseEntity<ApiResponse<PhotoResponse>> getPhoto(@PathVariable UUID photoId) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    PhotoResponse response = photoService.getPhoto(userId, photoId);

    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @DeleteMapping("/{photoId}")
  public ResponseEntity<ApiResponse<Void>> deletePhoto(@PathVariable UUID photoId) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    photoService.deletePhoto(userId, photoId);

    return ResponseEntity.ok(ApiResponse.successWithMessage("사진이 삭제되었습니다."));
  }
}
