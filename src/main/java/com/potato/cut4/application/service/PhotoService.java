package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.common.service.FileUploadService;
import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.Photo;
import com.potato.cut4.persistence.domain.PhotoCut;
import com.potato.cut4.persistence.domain.PhotoCutTemp;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.repository.FrameRepository;
import com.potato.cut4.persistence.repository.PhotoCutRepository;
import com.potato.cut4.persistence.repository.PhotoCutTempRepository;
import com.potato.cut4.persistence.repository.PhotoRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import com.potato.cut4.presentation.dto.response.PhotoCutResponse;
import com.potato.cut4.presentation.dto.response.PhotoResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

  private final PhotoRepository photoRepository;
  private final PhotoCutRepository photoCutRepository;
  private final PhotoCutTempRepository photoCutTempRepository;
  private final UserRepository userRepository;
  private final FrameRepository frameRepository;
  private final FileUploadService fileUploadService;

  @Transactional
  public List<PhotoCutResponse> uploadPhotoCuts(UUID userId, List<MultipartFile> cutImages) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    List<PhotoCutResponse> responses = new ArrayList<>();

    for (int i = 0; i < cutImages.size(); i++) {
      MultipartFile file = cutImages.get(i);
      String imageUrl = fileUploadService.uploadImage(file, "photo-cuts");

      PhotoCutTemp temp = PhotoCutTemp.builder()
          .user(user)
          .cutOrder(i + 1)
          .originalImageUrl(imageUrl)
          .build();

      temp = photoCutTempRepository.save(temp);

      PhotoCutResponse response = PhotoCutResponse.builder()
          .cutId(temp.getId())
          .cutOrder(temp.getCutOrder())
          .originalImageUrl(temp.getOriginalImageUrl())
          .build();

      responses.add(response);
    }

    log.info("Photo cuts uploaded: userId={}, count={}", userId, cutImages.size());

    return responses;
  }

  @Transactional
  public PhotoResponse savePhoto(UUID userId, UUID frameId, MultipartFile composedImage,
      List<UUID> photoCutIds) {

    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Frame frame = null;
    if (frameId != null) {
      frame = frameRepository.findById(frameId)
          .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));
    }

    List<PhotoCutTemp> tempCuts = photoCutTempRepository.findByIdIn(photoCutIds);
    if (tempCuts.size() != photoCutIds.size()) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "유효하지 않은 사진 ID가 포함되어 있습니다.");
    }

    // 권한 확인: 모든 임시 사진이 현재 사용자의 것인지
    boolean allBelongsToUser = tempCuts.stream()
        .allMatch(temp -> temp.getUser().getId().equals(userId));
    if (!allBelongsToUser) {
      throw new CustomException(ErrorCode.FORBIDDEN, "다른 사용자의 사진을 사용할 수 없습니다.");
    }

    String composedImageUrl = fileUploadService.uploadImage(composedImage, "photos");

    Photo photo = Photo.builder()
        .user(user)
        .frame(frame)
        .composedImageUrl(composedImageUrl)
        .build();

    photo = photoRepository.save(photo);

    for (PhotoCutTemp temp : tempCuts) {
      PhotoCut photoCut = PhotoCut.builder()
          .photo(photo)
          .cutOrder(temp.getCutOrder())
          .originalImageUrl(temp.getOriginalImageUrl())
          .build();

      photoCutRepository.save(photoCut);
    }

    photoCutTempRepository.deleteAll(tempCuts);

    log.info("Photo saved: photoId={}, userId={}, frameId={}", photo.getId(), userId, frameId);

    Photo savedPhoto = photoRepository.findByIdAndDeletedFalseWithCuts(photo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

    return PhotoResponse.from(savedPhoto);
  }

  @Transactional
  public void deletePhoto(UUID userId, UUID photoId) {
    Photo photo = photoRepository.findByIdAndDeletedFalse(photoId)
        .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

    if (!photo.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.PHOTO_ACCESS_DENIED);
    }

    fileUploadService.deleteImage(photo.getComposedImageUrl());
    for (PhotoCut cut : photo.getPhotoCuts()) {
      fileUploadService.deleteImage(cut.getOriginalImageUrl());
    }

    photo.delete();

    log.info("Photo deleted: photoId={}", photoId);
  }

  public PhotoResponse getPhoto(UUID userId, UUID photoId) {
    Photo photo = photoRepository.findByIdAndDeletedFalseWithCuts(photoId)
        .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

    if (!photo.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.PHOTO_ACCESS_DENIED);
    }

    return PhotoResponse.from(photo);
  }
}
