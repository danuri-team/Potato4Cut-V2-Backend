package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.PhotoService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.presentation.dto.response.PhotoCutResponse;
import com.potato.cut4.presentation.dto.response.PhotoResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
public class PhotoController {

  private final PhotoService photoService;

  @PostMapping(value = "/cuts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<List<PhotoCutResponse>>> uploadPhotoCuts(
      @RequestPart("images") List<MultipartFile> images) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    List<PhotoCutResponse> responses = photoService.uploadPhotoCuts(userId, images);

    return ResponseEntity.ok(ApiResponse.success(responses, "개별 사진이 업로드되었습니다."));
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<PhotoResponse>> savePhoto(
      @RequestParam(required = false) UUID frameId,
      @RequestPart("composedImage") MultipartFile composedImage,
      @RequestPart("photoCutIds") List<UUID> photoCutIds) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    PhotoResponse response = photoService.savePhoto(userId, frameId, composedImage, photoCutIds);

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
