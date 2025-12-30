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
import com.potato.cut4.presentation.dto.request.CreatePreSignedUrl;
import com.potato.cut4.presentation.dto.request.UpdateFrameRequest;
import com.potato.cut4.presentation.dto.response.FrameDetailResponse;
import com.potato.cut4.presentation.dto.response.FrameListResponse;
import com.potato.cut4.presentation.dto.response.PreSignedUrlResponse;
import java.util.Optional;
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
public class FrameService {

  private final FrameRepository frameRepository;
  private final UserRepository userRepository;
  private final FrameLikeRepository frameLikeRepository;
  private final UserFrameLibraryRepository libraryRepository;
  private final FileUploadService fileUploadService;
  private final UserFrameLibraryService libraryService;
  private final CreatorService creatorService;
  private final TagService tagService;

  public PreSignedUrlResponse generateFrameImagePreSignedUrl(CreatePreSignedUrl request) {
    return fileUploadService.generatePreSignedUrlForUpload("frames", request.fileSize());
  }

  public PreSignedUrlResponse generatePreviewImagePreSignedUrl(CreatePreSignedUrl request) {
    return fileUploadService.generatePreSignedUrlForUpload("previews", request.fileSize());
  }

  @Transactional
  public FrameDetailResponse createFrame(UUID userId, CreateFrameRequest request) {

    // 크리에이터 권한 확인
    creatorService.validateCreatorAccess(userId);
    Creator creator = creatorService.getCreatorByUserId(userId);

    // 이미지 URL 생성
    String frameBaseImageUrl = fileUploadService.buildImageUrl(request.getFrameBaseImageKey());
    String frameOverlayImageUrl = fileUploadService.buildImageUrl(
        request.getFrameOverlayImageKey());
    String previewImageUrl = fileUploadService.buildImageUrl(request.getPreviewImageKey());

    // 프레임 생성
    Frame frame = Frame.builder()
        .creator(creator)
        .title(request.getTitle())
        .description(request.getDescription())
        .frameBaseImageUrl(frameBaseImageUrl)
        .frameOverlayImageUrl(frameOverlayImageUrl)
        .previewImageUrl(previewImageUrl)
        .category(request.getCategory())
        .status(FrameStatus.PENDING)
        .price(request.getPrice())
        .isPublic(request.getIsPublic())
        .build();

    frame = frameRepository.save(frame);

    tagService.addTagsToFrame(frame, request.getTags());

    libraryService.addToLibrary(userId, frame.getId());

    log.info("Frame created: frameId={}, creatorId={}", frame.getId(), creator.getId());

    return FrameDetailResponse.from(frame, false, true);
  }

  @Transactional
  public FrameDetailResponse updateFrame(UUID userId, UUID frameId, UpdateFrameRequest request) {

    Creator creator = creatorService.getCreatorByUserId(userId);
    Frame frame = frameRepository.findByIdAndCreator(frameId, creator)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_ACCESS_DENIED));

    if (request.getFrameBaseImageKey() != null) {
      fileUploadService.deleteImage(frame.getFrameBaseImageUrl());
      String newUrl = fileUploadService.buildImageUrl(request.getFrameBaseImageKey());
      frame.updateFrameBaseImageUrl(newUrl);
    }

    if (request.getFrameOverlayImageKey() != null) {
      fileUploadService.deleteImage(frame.getFrameOverlayImageUrl());
      String newUrl = fileUploadService.buildImageUrl(request.getFrameOverlayImageKey());
      frame.updateFrameOverlayImageUrl(newUrl);
    }

    if (request.getPreviewImageKey() != null) {
      fileUploadService.deleteImage(frame.getPreviewImageUrl());
      String newUrl = fileUploadService.buildImageUrl(request.getPreviewImageKey());
      frame.updatePreviewImageUrl(newUrl);
    }

    // 프레임 정보 업데이트
    frame.updateInfo(request.getTitle(), request.getDescription(), request.getCategory(),
        request.getPrice(), request.getIsPublic());

    // 태그 업데이트
    if (request.getTags() != null) {
      tagService.updateFrameTags(frame, request.getTags());
    }

    log.info("Frame updated: frameId={}", frameId);

    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    boolean isLiked = frameLikeRepository.existsByUserAndFrame(user, frame);

    return FrameDetailResponse.from(frame, isLiked, true);
  }

  @Transactional
  public void deleteFrame(UUID userId, UUID frameId) {
    Creator creator = creatorService.getCreatorByUserId(userId);
    Frame frame = frameRepository.findByIdAndCreator(frameId, creator)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_ACCESS_DENIED));

    fileUploadService.deleteImage(frame.getFrameBaseImageUrl());
    fileUploadService.deleteImage(frame.getFrameOverlayImageUrl());
    fileUploadService.deleteImage(frame.getPreviewImageUrl());

    frame.hide();

    log.info("Frame deleted: frameId={}", frameId);
  }

  public Page<FrameListResponse> getFrames(FrameCategory category, Pageable pageable) {
    Page<Frame> frames;

    if (category != null) {
      frames = frameRepository.findByStatusAndCategoryAndIsPublicTrue(FrameStatus.APPROVED,
          category,
          pageable);
    } else {
      frames = frameRepository.findByStatusAndIsPublicTrue(FrameStatus.APPROVED, pageable);
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
