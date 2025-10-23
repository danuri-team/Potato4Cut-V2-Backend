package com.potato.cut4.persistence.domain.type;

public enum ReportStatus {
  PENDING,    // 처리 대기
  ACCEPTED,   // 신고 승인 (조치 완료)
  REJECTED    // 신고 기각
}
