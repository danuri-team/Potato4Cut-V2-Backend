package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.UserFrameLibrary;
import com.potato.cut4.persistence.repository.FrameRepository;
import com.potato.cut4.persistence.repository.UserFrameLibraryRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import com.potato.cut4.presentation.dto.response.LibraryFrameResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFrameLibraryService {

  private final UserFrameLibraryRepository libraryRepository;
  private final UserRepository userRepository;
  private final FrameRepository frameRepository;

  @Transactional
  public LibraryFrameResponse addToLibrary(UUID userId, UUID frameId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Frame frame = frameRepository.findById(frameId)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));

    // 이미 라이브러리에 있는지 확인
    if (libraryRepository.existsByUserAndFrame(user, frame)) {
      throw new CustomException(ErrorCode.FRAME_ALREADY_IN_LIBRARY);
    }

    // 라이브러리에 추가
    UserFrameLibrary library = UserFrameLibrary.builder()
        .user(user)
        .frame(frame)
        .bookmarked(false)
        .build();

    library = libraryRepository.save(library);

    // 다운로드 수 증가
    frame.incrementDownloadCount();

    log.info("Frame added to library: userId={}, frameId={}", userId, frameId);

    return LibraryFrameResponse.from(library);
  }

  @Transactional
  public void removeFromLibrary(UUID userId, UUID frameId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Frame frame = frameRepository.findById(frameId)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));

    UserFrameLibrary library = libraryRepository.findByUserAndFrame(user, frame)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_IN_LIBRARY));

    libraryRepository.delete(library);

    log.info("Frame removed from library: userId={}, frameId={}", userId, frameId);
  }

  @Transactional
  public LibraryFrameResponse toggleBookmark(UUID userId, UUID frameId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Frame frame = frameRepository.findById(frameId)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));

    UserFrameLibrary library = libraryRepository.findByUserAndFrame(user, frame)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_IN_LIBRARY));

    library.toggleBookmark();

    log.info("Bookmark toggled: userId={}, frameId={}, bookmarked={}", userId, frameId,
        library.isBookmarked());

    return LibraryFrameResponse.from(library);
  }

  @Transactional
  public void updateLastUsed(UUID userId, UUID frameId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Frame frame = frameRepository.findById(frameId)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));

    UserFrameLibrary library = libraryRepository.findByUserAndFrame(user, frame)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_IN_LIBRARY));

    library.updateLastUsedAt();

    log.info("Last used updated: userId={}, frameId={}", userId, frameId);
  }

  public Page<LibraryFrameResponse> getMyLibrary(UUID userId, boolean bookmarkedOnly,
      Pageable pageable) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Page<UserFrameLibrary> libraries;
    if (bookmarkedOnly) {
      libraries = libraryRepository.findByUserAndBookmarkedTrue(user, pageable);
    } else {
      libraries = libraryRepository.findByUser(user, pageable);
    }

    return libraries.map(LibraryFrameResponse::from);
  }
}
