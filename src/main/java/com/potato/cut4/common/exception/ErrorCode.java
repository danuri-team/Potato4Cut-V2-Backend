package com.potato.cut4.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // Common
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류가 발생했습니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C003", "인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "C004", "권한이 없습니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "요청한 리소스를 찾을 수 없습니다."),

  // User
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
  DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "U002", "이미 사용 중인 닉네임입니다."),
  USER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "U003", "이미 삭제된 사용자입니다."),

  // Creator
  CREATOR_NOT_FOUND(HttpStatus.NOT_FOUND, "CR001", "크리에이터를 찾을 수 없습니다."),
  CREATOR_ALREADY_APPLIED(HttpStatus.CONFLICT, "CR002", "이미 크리에이터 신청을 하셨습니다."),
  CREATOR_NOT_APPROVED(HttpStatus.FORBIDDEN, "CR003", "승인된 크리에이터만 접근 가능합니다."),
  CREATOR_ALREADY_APPROVED(HttpStatus.CONFLICT, "CR004", "이미 승인된 크리에이터입니다."),

  // Frame
  FRAME_NOT_FOUND(HttpStatus.NOT_FOUND, "F001", "프레임을 찾을 수 없습니다."),
  FRAME_NOT_APPROVED(HttpStatus.BAD_REQUEST, "F002", "승인되지 않은 프레임입니다."),
  FRAME_ALREADY_LIKED(HttpStatus.CONFLICT, "F003", "이미 좋아요한 프레임입니다."),
  FRAME_NOT_LIKED(HttpStatus.BAD_REQUEST, "F004", "좋아요하지 않은 프레임입니다."),
  FRAME_ACCESS_DENIED(HttpStatus.FORBIDDEN, "F005", "프레임 접근 권한이 없습니다."),

  // Comment
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CM001", "댓글을 찾을 수 없습니다."),
  COMMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "CM002", "댓글 접근 권한이 없습니다."),

  // Photo
  PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "사진을 찾을 수 없습니다."),
  PHOTO_ACCESS_DENIED(HttpStatus.FORBIDDEN, "P002", "사진 접근 권한이 없습니다."),

  // Tag
  TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "태그를 찾을 수 없습니다."),

  // Library
  FRAME_ALREADY_IN_LIBRARY(HttpStatus.CONFLICT, "L001", "이미 라이브러리에 있는 프레임입니다."),
  FRAME_NOT_IN_LIBRARY(HttpStatus.NOT_FOUND, "L002", "라이브러리에 없는 프레임입니다."),

  // Report
  REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "신고를 찾을 수 없습니다."),
  REPORT_ALREADY_PROCESSED(HttpStatus.CONFLICT, "R002", "이미 처리된 신고입니다."),

  // File
  FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FL001", "파일 업로드에 실패했습니다."),
  FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FL002", "파일 삭제에 실패했습니다."),
  INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "FL003", "지원하지 않는 파일 형식입니다."),
  FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "FL004", "파일 크기가 제한을 초과했습니다."),

  // Notification
  NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "알림을 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
