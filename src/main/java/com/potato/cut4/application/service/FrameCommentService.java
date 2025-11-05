package com.potato.cut4.application.service;

import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.FrameComment;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.repository.FrameCommentRepository;
import com.potato.cut4.persistence.repository.FrameRepository;
import com.potato.cut4.persistence.repository.UserRepository;
import com.potato.cut4.presentation.dto.request.CreateCommentRequest;
import com.potato.cut4.presentation.dto.request.UpdateCommentRequest;
import com.potato.cut4.presentation.dto.response.CommentResponse;
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
public class FrameCommentService {

  private final FrameCommentRepository commentRepository;
  private final FrameRepository frameRepository;
  private final UserRepository userRepository;

  @Transactional
  public CommentResponse createComment(UUID userId, UUID frameId, CreateCommentRequest request) {
    User user = userRepository.findByIdAndDeletedFalse(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Frame frame = frameRepository.findById(frameId)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));

    FrameComment comment = FrameComment.builder()
        .user(user)
        .frame(frame)
        .content(request.getContent())
        .build();

    comment = commentRepository.save(comment);

    log.info("Comment created: commentId={}, userId={}, frameId={}", comment.getId(), userId,
        frameId);

    return CommentResponse.from(comment, userId);
  }

  @Transactional
  public CommentResponse updateComment(UUID userId, UUID commentId,
      UpdateCommentRequest request) {
    FrameComment comment = commentRepository.findByIdAndDeletedFalse(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    // 댓글 작성자 확인
    if (!comment.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
    }

    comment.updateContent(request.getContent());

    log.info("Comment updated: commentId={}", commentId);

    return CommentResponse.from(comment, userId);
  }

  @Transactional
  public void deleteComment(UUID userId, UUID commentId) {
    FrameComment comment = commentRepository.findByIdAndDeletedFalse(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    if (!comment.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
    }

    comment.delete();

    log.info("Comment deleted: commentId={}", commentId);
  }

  public Page<CommentResponse> getComments(UUID frameId, UUID currentUserId, Pageable pageable) {
    Frame frame = frameRepository.findById(frameId)
        .orElseThrow(() -> new CustomException(ErrorCode.FRAME_NOT_FOUND));

    Page<FrameComment> comments = commentRepository.findByFrameAndDeletedFalse(frame, pageable);
    return comments.map(comment -> CommentResponse.from(comment, currentUserId));
  }
}
