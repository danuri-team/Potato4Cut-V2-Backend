package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.persistence.domain.Creator;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.type.CreatorStatus;
import com.potato.cut4.persistence.domain.type.UserRole;
import com.potato.cut4.persistence.repository.CreatorRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import com.potato.cut4.presentation.dto.response.CreatorResponse;
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
public class CreatorService {

  private final CreatorRepository creatorRepository;
  private final UserRepository userRepository;

  @Transactional
  public void applyForCreator(UUID userId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (creatorRepository.existsByUser(user)) {
      throw new CustomException(ErrorCode.CREATOR_ALREADY_APPLIED);
    }

    Creator creator = Creator.builder()
        .user(user)
        .status(CreatorStatus.PENDING)
        .build();

    creator = creatorRepository.save(creator);
    log.info("Creator application submitted: userId={}, creatorId={}", userId, creator.getId());
  }

  public CreatorResponse getCreatorInfo(UUID userId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Creator creator = creatorRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_NOT_FOUND));

    return CreatorResponse.from(creator);
  }

  public Creator getCreatorByUserId(UUID userId) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return creatorRepository.findByUser(user)
        .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_NOT_FOUND));
  }

  @Transactional
  public CreatorResponse approveCreator(UUID creatorId) {
    Creator creator = creatorRepository.findById(creatorId)
        .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_NOT_FOUND));

    if (creator.getStatus() == CreatorStatus.APPROVED) {
      throw new CustomException(ErrorCode.CREATOR_ALREADY_APPROVED);
    }

    creator.approve();

    creator.getUser().updateRole(UserRole.CREATOR);

    log.info("Creator approved: creatorId={}, userId={}", creatorId, creator.getUser().getId());

    return CreatorResponse.from(creator);
  }

  @Transactional
  public CreatorResponse rejectCreator(UUID creatorId, String rejectionReason) {
    Creator creator = creatorRepository.findById(creatorId)
        .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_NOT_FOUND));

    creator.reject(rejectionReason);

    log.info("Creator rejected: creatorId={}, reason={}", creatorId, rejectionReason);

    return CreatorResponse.from(creator);
  }

  public Page<CreatorResponse> getPendingCreators(Pageable pageable) {
    Page<Creator> creators = creatorRepository.findByStatus(CreatorStatus.PENDING, pageable);
    return creators.map(CreatorResponse::from);
  }

  public void validateCreatorAccess(UUID userId) {
    Creator creator = getCreatorByUserId(userId);

    if (creator.getStatus() != CreatorStatus.APPROVED) {
      throw new CustomException(ErrorCode.CREATOR_NOT_APPROVED);
    }
  }
}
