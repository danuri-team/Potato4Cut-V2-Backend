package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.common.service.FileUploadService;
import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.Photo;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.repository.FrameRepository;
import com.potato.cut4.persistence.repository.PhotoRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import com.potato.cut4.presentation.dto.request.CreatePreSignedUrl;
import com.potato.cut4.presentation.dto.response.PhotoResponse;
import com.potato.cut4.presentation.dto.response.PreSignedUrlResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

  private final PhotoRepository photoRepository;
  private final UserRepository userRepository;
  private final FrameRepository frameRepository;
  private final FileUploadService fileUploadService;

  public PreSignedUrlResponse generatePreSignedUrl(CreatePreSignedUrl request) {
    return fileUploadService.generatePreSignedUrlForUpload("photos", request.fileSize());
  }

  @Transactional
  public PhotoResponse savePhoto(UUID userId, UUID frameId, String objectKey) {

    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Frame frame = null;
    if (frameId != null) {
      frame = frameRepository.findById(frameId)
          .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));
    }

    String composedImageUrl = fileUploadService.buildImageUrl(objectKey);

    Photo photo = Photo.builder()
        .user(user)
        .frame(frame)
        .composedImageUrl(composedImageUrl)
        .build();

    photo = photoRepository.save(photo);

    log.info("Photo saved: photoId={}, userId={}, frameId={}", photo.getId(), userId, frameId);

    return PhotoResponse.from(photo);
  }

  @Transactional
  public void deletePhoto(UUID userId, UUID photoId) {
    Photo photo = photoRepository.findByIdAndDeletedFalse(photoId)
        .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

    if (!photo.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.PHOTO_ACCESS_DENIED);
    }

    fileUploadService.deleteImage(photo.getComposedImageUrl());

    photo.delete();

    log.info("Photo deleted: photoId={}", photoId);
  }

  public PhotoResponse getPhoto(UUID userId, UUID photoId) {
    Photo photo = photoRepository.findByIdAndDeletedFalse(photoId)
        .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

    if (!photo.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.PHOTO_ACCESS_DENIED);
    }

    return PhotoResponse.from(photo);
  }
}
