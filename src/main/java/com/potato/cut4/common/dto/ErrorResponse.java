package com.potato.cut4.common.dto;

import com.potato.cut4.common.exception.ErrorCode;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

  private final String code;
  private final String message;
  private final int status;
  private final LocalDateTime timestamp;

  public static ErrorResponse of(ErrorCode errorCode) {
    return ErrorResponse.builder()
        .code(errorCode.getCode())
        .message(errorCode.getMessage())
        .status(errorCode.getStatus().value())
        .timestamp(LocalDateTime.now())
        .build();
  }

  public static ErrorResponse of(ErrorCode errorCode, String message) {
    return ErrorResponse.builder()
        .code(errorCode.getCode())
        .message(message)
        .status(errorCode.getStatus().value())
        .timestamp(LocalDateTime.now())
        .build();
  }
}
