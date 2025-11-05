package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.common.security.AuthenticationUtil;
import com.potato.cut4.common.service.FileUploadService;
import com.potato.cut4.persistence.domain.Creator;
import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.type.FrameCategory;
import com.potato.cut4.persistence.domain.type.FrameStatus;
import com.potato.cut4.persistence.repository.FrameLikeRepository;
import com.potato.cut4.persistence.repository.FrameRepository;
import com.potato.cut4.persistence.repository.UserFrameLibraryRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import com.potato.cut4.presentation.dto.request.CreateFrameRequest;
import com.potato.cut4.presentation.dto.request.UpdateFrameRequest;
import com.potato.cut4.presentation.dto.response.FrameDetailResponse;
import com.potato.cut4.presentation.dto.response.FrameListResponse;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FrameService {

  private final FrameRepository frameRepository;
  private final UserRepository userRepository;
  private final FrameLikeRepository frameLikeRepository;
  private final UserFrameLibraryRepository libraryRepository;
  private final FileUploadService fileUploadService;
  private final CreatorService creatorService;
  private final TagService tagService;

  @Transactional
  public FrameDetailResponse createFrame(UUID userId, CreateFrameRequest request,
      MultipartFile frameImage, MultipartFile previewImage) {

    // 크리에이터 권한 확인
    creatorService.validateCreatorAccess(userId);
    Creator creator = creatorService.getCreatorByUserId(userId);

    // 이미지 업로드
    String frameImageUrl = fileUploadService.uploadImage(frameImage, "frames");
    String previewImageUrl = fileUploadService.uploadImage(previewImage, "previews");

    // 프레임 생성
    Frame frame = Frame.builder()
        .creator(creator)
        .title(request.getTitle())
        .description(request.getDescription())
        .frameImageUrl(frameImageUrl)
        .previewImageUrl(previewImageUrl)
        .category(request.getCategory())
        .status(FrameStatus.PENDING)
        .build();

    frame = frameRepository.save(frame);

    tagService.addTagsToFrame(frame, request.getTags());

    log.info("Frame created: frameId={}, creatorId={}", frame.getId(), creator.getId());

    return FrameDetailResponse.from(frame, false, false);
  }

  @Transactional
  public FrameDetailResponse updateFrame(UUID userId, UUID frameId, UpdateFrameRequest request,
      MultipartFile frameImage, MultipartFile previewImage) {

    Creator creator = creatorService.getCreatorByUserId(userId);
    Frame frame = frameRepository.findByIdAndCreator(frameId, creator)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_ACCESS_DENIED));

    // 이미지 업데이트
    if (frameImage != null && !frameImage.isEmpty()) {
      fileUploadService.deleteImage(frame.getFrameImageUrl());
      fileUploadService.uploadImage(frameImage, "frames");
      frame.updateInfo(request.getTitle(), request.getDescription(), request.getCategory());
    }

    if (previewImage != null && !previewImage.isEmpty()) {
      fileUploadService.deleteImage(frame.getPreviewImageUrl());
      fileUploadService.uploadImage(previewImage, "previews");
    }

    // 프레임 정보 업데이트
    frame.updateInfo(request.getTitle(), request.getDescription(), request.getCategory());

    // 태그 업데이트
    if (request.getTags() != null) {
      tagService.updateFrameTags(frame, request.getTags());
    }

    log.info("Frame updated: frameId={}", frameId);

    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    boolean isLiked = frameLikeRepository.existsByUserAndFrame(user, frame);
    boolean isInLibrary = libraryRepository.existsByUserAndFrame(user, frame);

    return FrameDetailResponse.from(frame, isLiked, isInLibrary);
  }

  @Transactional
  public void deleteFrame(UUID userId, UUID frameId) {
    Creator creator = creatorService.getCreatorByUserId(userId);
    Frame frame = frameRepository.findByIdAndCreator(frameId, creator)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_ACCESS_DENIED));

    fileUploadService.deleteImage(frame.getFrameImageUrl());
    fileUploadService.deleteImage(frame.getPreviewImageUrl());

    frame.hide();

    log.info("Frame deleted: frameId={}", frameId);
  }

  public Page<FrameListResponse> getFrames(FrameCategory category, Pageable pageable) {
    Page<Frame> frames;

    if (category != null) {
      frames = frameRepository.findByStatusAndCategory(FrameStatus.APPROVED, category, pageable);
    } else {
      frames = frameRepository.findByStatus(FrameStatus.APPROVED, pageable);
    }

    return frames.map(FrameListResponse::from);
  }

  @Transactional
  public FrameDetailResponse getFrameDetail(UUID frameId) {
    Frame frame = frameRepository.findById(frameId)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));

    frame.incrementViewCount();

    boolean isLiked = false;
    boolean isInLibrary = false;
    Optional<UUID> userId = Optional.empty();

    try {
      userId = Optional.ofNullable(AuthenticationUtil.getCurrentUserId());
    } catch (Exception ignored) {
      // 미 가입 유저의 경우, 좋아요와 라이브러리 존재 여부를 확인 X
    }

    if (userId.isPresent()) {
      User user = userRepository.findByIdAndDeletedFalse(userId.get())
          .orElse(null);

      if (user != null) {
        isLiked = frameLikeRepository.existsByUserAndFrame(user, frame);
        isInLibrary = libraryRepository.existsByUserAndFrame(user, frame);
      }
    }

    return FrameDetailResponse.from(frame, isLiked, isInLibrary);
  }

  public Page<FrameListResponse> searchFrames(String keyword, Pageable pageable) {
    Page<Frame> frames = frameRepository.searchByKeyword(FrameStatus.APPROVED, keyword, pageable);
    return frames.map(FrameListResponse::from);
  }

  public Page<FrameListResponse> getFramesByTag(String tagName, Pageable pageable) {
    Page<Frame> frames = frameRepository.findByStatusAndTagName(FrameStatus.APPROVED, tagName,
        pageable);
    return frames.map(FrameListResponse::from);
  }

  public Page<FrameListResponse> getMyFrames(UUID userId, Pageable pageable) {
    Creator creator = creatorService.getCreatorByUserId(userId);
    Page<Frame> frames = frameRepository.findByCreator(creator, pageable);
    return frames.map(FrameListResponse::from);
  }
}
