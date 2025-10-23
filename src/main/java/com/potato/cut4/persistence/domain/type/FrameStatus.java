package com.potato.cut4.persistence.domain.type;

public enum FrameStatus {
  PENDING,    // 검수 대기
  APPROVED,   // 승인됨 (공개)
  REJECTED,   // 반려됨
  HIDDEN      // 숨김 (신고, 관리자 블라인드)
}
