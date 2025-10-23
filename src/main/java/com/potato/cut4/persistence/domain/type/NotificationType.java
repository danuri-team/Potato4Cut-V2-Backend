package com.potato.cut4.persistence.domain.type;

public enum NotificationType {
  COMMENT,        // 댓글 알림
  LIKE,           // 좋아요 알림
  FOLLOW,         // 팔로우 알림
  FRAME_APPROVED, // 프레임 승인 알림
  FRAME_REJECTED, // 프레임 반려 알림
  ANNOUNCEMENT    // 공지사항
}
