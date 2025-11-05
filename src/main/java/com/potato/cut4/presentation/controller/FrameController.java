package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.FrameCommentService;
import com.potato.cut4.application.service.FrameLikeService;
import com.potato.cut4.application.service.FrameService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.common.dto.PageResponse;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.persistence.domain.type.FrameCategory;
import com.potato.cut4.presentation.dto.request.CreateCommentRequest;
import com.potato.cut4.presentation.dto.request.CreateFrameRequest;
import com.potato.cut4.presentation.dto.request.UpdateCommentRequest;
import com.potato.cut4.presentation.dto.request.UpdateFrameRequest;
import com.potato.cut4.presentation.dto.response.CommentResponse;
import com.potato.cut4.presentation.dto.response.FrameDetailResponse;
import com.potato.cut4.presentation.dto.response.FrameListResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/frames")
@RequiredArgsConstructor
public class FrameController {

  private final FrameService frameService;
  private final FrameLikeService frameLikeService;
  private final FrameCommentService commentService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<FrameDetailResponse>> createFrame(
      @Valid @RequestPart("data") CreateFrameRequest request,
      @RequestPart("frameImage") MultipartFile frameImage,
      @RequestPart("previewImage") MultipartFile previewImage) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    FrameDetailResponse response = frameService.createFrame(userId, request, frameImage,
        previewImage);

    return ResponseEntity.ok(ApiResponse.success(response, "프레임이 등록되었습니다. 검수 후 공개됩니다."));
  }

  @PutMapping(value = "/{frameId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<FrameDetailResponse>> updateFrame(
      @PathVariable UUID frameId,
      @Valid @RequestPart("data") UpdateFrameRequest request,
      @RequestPart(value = "frameImage", required = false) MultipartFile frameImage,
      @RequestPart(value = "previewImage", required = false) MultipartFile previewImage) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    FrameDetailResponse response = frameService.updateFrame(userId, frameId, request, frameImage,
        previewImage);

    return ResponseEntity.ok(ApiResponse.success(response, "프레임이 수정되었습니다."));
  }

  @DeleteMapping("/{frameId}")
  public ResponseEntity<ApiResponse<Void>> deleteFrame(@PathVariable UUID frameId) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    frameService.deleteFrame(userId, frameId);
    return ResponseEntity.ok(ApiResponse.successWithMessage("프레임이 삭제되었습니다."));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<FrameListResponse>>> getFrames(
      @RequestParam(required = false) FrameCategory category,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
    Page<FrameListResponse> frames = frameService.getFrames(category, pageable);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(frames)));
  }

  @GetMapping("/{frameId}")
  public ResponseEntity<ApiResponse<FrameDetailResponse>> getFrameDetail(
      @PathVariable UUID frameId) {
    FrameDetailResponse response = frameService.getFrameDetail(frameId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<PageResponse<FrameListResponse>>> searchFrames(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<FrameListResponse> frames = frameService.searchFrames(keyword, pageable);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(frames)));
  }

  @GetMapping("/tags/{tagName}")
  public ResponseEntity<ApiResponse<PageResponse<FrameListResponse>>> getFramesByTag(
      @PathVariable String tagName,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<FrameListResponse> frames = frameService.getFramesByTag(tagName, pageable);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(frames)));
  }

  @GetMapping("/my")
  public ResponseEntity<ApiResponse<PageResponse<FrameListResponse>>> getMyFrames(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<FrameListResponse> frames = frameService.getMyFrames(userId, pageable);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(frames)));
  }

  // === 좋아요 API ===

  @PostMapping("/{frameId}/like")
  public ResponseEntity<ApiResponse<Void>> likeFrame(@PathVariable UUID frameId) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    frameLikeService.likeFrame(userId, frameId);
    return ResponseEntity.ok(ApiResponse.success());
  }

  @DeleteMapping("/{frameId}/like")
  public ResponseEntity<ApiResponse<Void>> unlikeFrame(@PathVariable UUID frameId) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    frameLikeService.unlikeFrame(userId, frameId);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // === 댓글 API ===

  @PostMapping("/{frameId}/comments")
  public ResponseEntity<ApiResponse<CommentResponse>> createComment(
      @PathVariable UUID frameId,
      @Valid @RequestBody CreateCommentRequest request) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    CommentResponse response = commentService.createComment(userId, frameId, request);
    return ResponseEntity.ok(ApiResponse.success(response, "댓글이 작성되었습니다."));
  }

  @GetMapping("/{frameId}/comments")
  public ResponseEntity<ApiResponse<PageResponse<CommentResponse>>> getComments(
      @PathVariable UUID frameId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    UUID currentUserId = null;
    try {
      currentUserId = AuthenticationUtil.getCurrentUserId();
    } catch (Exception e) {
      // 비로그인 사용자도 접근 가능
    }

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<CommentResponse> comments = commentService.getComments(frameId, currentUserId, pageable);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(comments)));
  }

  @PutMapping("/comments/{commentId}")
  public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
      @PathVariable UUID commentId,
      @Valid @RequestBody UpdateCommentRequest request) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    CommentResponse response = commentService.updateComment(userId, commentId, request);
    return ResponseEntity.ok(ApiResponse.success(response, "댓글이 수정되었습니다."));
  }

  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable UUID commentId) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    commentService.deleteComment(userId, commentId);
    return ResponseEntity.ok(ApiResponse.successWithMessage("댓글이 삭제되었습니다."));
  }
}
