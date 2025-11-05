package com.potato.cut4.presentation.controller;

import com.potato.cut4.application.service.UserFrameLibraryService;
import com.potato.cut4.common.dto.ApiResponse;
import com.potato.cut4.common.dto.PageResponse;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.presentation.dto.response.LibraryFrameResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
public class LibraryController {

  private final UserFrameLibraryService libraryService;

  @PostMapping("/frames/{frameId}")
  public ResponseEntity<ApiResponse<LibraryFrameResponse>> addFrameToLibrary(
      @PathVariable UUID frameId) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    LibraryFrameResponse response = libraryService.addToLibrary(userId, frameId);

    return ResponseEntity.ok(ApiResponse.success(response, "프레임이 라이브러리에 추가되었습니다."));
  }

  @DeleteMapping("/frames/{frameId}")
  public ResponseEntity<ApiResponse<Void>> removeFrameFromLibrary(@PathVariable UUID frameId) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    libraryService.removeFromLibrary(userId, frameId);

    return ResponseEntity.ok(ApiResponse.successWithMessage("프레임이 라이브러리에서 제거되었습니다."));
  }

  @PutMapping("/frames/{frameId}/bookmark")
  public ResponseEntity<ApiResponse<LibraryFrameResponse>> toggleBookmark(
      @PathVariable UUID frameId) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    LibraryFrameResponse response = libraryService.toggleBookmark(userId, frameId);

    String message =
        response.isBookmarked() ? "북마크에 추가되었습니다." : "북마크에서 제거되었습니다.";
    return ResponseEntity.ok(ApiResponse.success(response, message));
  }

  @PutMapping("/frames/{frameId}/use")
  public ResponseEntity<ApiResponse<Void>> updateLastUsed(@PathVariable UUID frameId) {
    UUID userId = AuthenticationUtil.getCurrentUserId();
    libraryService.updateLastUsed(userId, frameId);

    return ResponseEntity.ok(ApiResponse.successWithMessage("최근 사용 시간이 업데이트되었습니다."));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<LibraryFrameResponse>>> getMyLibrary(
      @RequestParam(defaultValue = "false") boolean bookmarkedOnly,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "addedAt") String sortBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

    UUID userId = AuthenticationUtil.getCurrentUserId();
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
    Page<LibraryFrameResponse> library = libraryService.getMyLibrary(userId, bookmarkedOnly,
        pageable);

    return ResponseEntity.ok(ApiResponse.success(PageResponse.of(library)));
  }
}
