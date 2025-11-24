package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.common.service.FileUploadService;
import com.potato.cut4.persistence.domain.Photo;
import com.potato.cut4.persistence.domain.ProfilePreset;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.repository.PhotoRepository;
import com.potato.cut4.persistence.repository.ProfilePresetRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import com.potato.cut4.presentation.dto.request.UpdateProfileRequest;
import com.potato.cut4.presentation.dto.response.PhotoListResponse;
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
public class UserService {

  private final UserRepository userRepository;
  private final PhotoRepository photoRepository;
  private final FileUploadService fileUploadService;
  private final ProfilePresetRepository profilePresetRepository;

  public User getUserById(UUID userId) {
    return userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

  @Transactional
  public User updateProfile(UUID userId, UpdateProfileRequest request,
      MultipartFile profileImage) {
    User user = getUserById(userId);

    // 닉네임 중복 체크
    if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
      if (userRepository.existsByNickname(request.getNickname())) {
        throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
      }
    }

    String profileImageUrl = user.getProfileImageUrl();

    if (request.getProfilePresetId() != null) {
      ProfilePreset profilePreset = profilePresetRepository.findById(request.getProfilePresetId())
          .orElseThrow(() -> new CustomException(ErrorCode.PRESET_NOT_FOUND));
      profileImageUrl = profilePreset.getImgUrl();
    }

    if (profileImage != null && !profileImage.isEmpty()) {
      if (profileImageUrl != null) {
        try {
          if (!profilePresetRepository.existsProfilePresetByImgUrl(profileImageUrl)) {
            fileUploadService.deleteImage(profileImageUrl);
          }
        } catch (Exception e) {
          log.warn("Failed to delete old profile image: {}", profileImageUrl);
        }
      }
      profileImageUrl = fileUploadService.uploadImage(profileImage, "profiles");
    }

    user.updateProfile(request.getNickname(), profileImageUrl, request.getBio());
    return user;
  }

  @Transactional
  public void deleteAccount(UUID userId) {
    User user = getUserById(userId);

    if (user.isDeleted()) {
      throw new CustomException(ErrorCode.USER_ALREADY_DELETED);
    }

    user.delete();
    log.info("User account deleted: userId={}", userId);
  }

  public Page<PhotoListResponse> getMyPhotos(UUID userId, Pageable pageable) {
    User user = getUserById(userId);
    Page<Photo> photos = photoRepository.findByUserAndDeletedFalse(user, pageable);
    return photos.map(PhotoListResponse::from);
  }
}
