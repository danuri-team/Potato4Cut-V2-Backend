package com.potato.cut4.application.service;


import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.persistence.domain.Photo;
import com.potato.cut4.persistence.domain.Share;
import com.potato.cut4.persistence.domain.type.PhotoShareType;
import com.potato.cut4.persistence.repository.PhotoRepository;
import com.potato.cut4.persistence.repository.ShareRepository;
import com.potato.cut4.presentation.dto.request.UpdateShareRequest;
import com.potato.cut4.presentation.dto.response.PhotoResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShareService {

  private final ShareRepository shareRepository;
  private final PhotoRepository photoRepository;

  @Transactional
  public void updateShareStatus(UUID photoId, UUID userId, UpdateShareRequest request) {
    Photo photo = photoRepository.findByIdAndDeletedFalse(photoId)
        .orElseThrow(() -> new CustomException(ErrorCode.PHOTO_NOT_FOUND));

    if (!photo.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.PHOTO_ACCESS_DENIED);
    }

    Share share = photo.getShare();

    share.update(LocalDateTime.now().plusMinutes(request.getExpireAt()),
        request.getPhotoShareType());
  }

  public PhotoResponse getPhoto(String code) {
    Share share = shareRepository.findByCode(code).orElseThrow(() ->
        new CustomException(ErrorCode.NOT_FOUND
        )
    );

    if (share.getType() == PhotoShareType.PRIVATE) {
      throw new CustomException(ErrorCode.IS_PRIVATE);
    }

    if (share.getExpireAt().isBefore(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.EXPIRED_CODE);
    }

    return PhotoResponse.from(share.getPhoto());
  }
}
