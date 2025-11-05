package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.FrameLike;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.repository.FrameLikeRepository;
import com.potato.cut4.persistence.repository.FrameRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FrameLikeService {

  private final FrameLikeRepository frameLikeRepository;
  private final FrameRepository frameRepository;
  private final UserRepository userRepository;

  @Transactional
  public void likeFrame(UUID userId, UUID frameId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Frame frame = frameRepository.findById(frameId)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));

    if (frameLikeRepository.existsByUserAndFrame(user, frame)) {
      throw new CustomException(ErrorCode.FRAME_ALREADY_LIKED);
    }

    FrameLike frameLike = FrameLike.builder()
        .user(user)
        .frame(frame)
        .build();
    frameLikeRepository.save(frameLike);

    frame.incrementLikeCount();

    log.info("Frame liked: userId={}, frameId={}", userId, frameId);
  }

  @Transactional
  public void unlikeFrame(UUID userId, UUID frameId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Frame frame = frameRepository.findById(frameId)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));

    FrameLike frameLike = frameLikeRepository.findByUserAndFrame(user, frame)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_LIKED));

    frameLikeRepository.delete(frameLike);

    frame.decrementLikeCount();

    log.info("Frame unliked: userId={}, frameId={}", userId, frameId);
  }
}
