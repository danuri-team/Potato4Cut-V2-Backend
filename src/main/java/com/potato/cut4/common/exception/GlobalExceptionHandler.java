package com.potato.cut4.common.exception;

import com.potato.cut4.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    log.error("CustomException: code={}, message={}", e.getErrorCode().getCode(),
        e.getMessage());
    ErrorResponse response = ErrorResponse.of(e.getErrorCode(), e.getMessage());
    return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {
    FieldError fieldError = e.getBindingResult().getFieldError();
    String message =
        fieldError != null ? fieldError.getDefaultMessage() : ErrorCode.INVALID_INPUT_VALUE.getMessage();

    log.error("Validation error: {}", message);
    ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, message);
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
    log.error("AuthenticationException: {}", e.getMessage());
    ErrorResponse response = ErrorResponse.of(ErrorCode.UNAUTHORIZED);
    return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatus()).body(response);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    log.error("AccessDeniedException: {}", e.getMessage());
    ErrorResponse response = ErrorResponse.of(ErrorCode.FORBIDDEN);
    return ResponseEntity.status(ErrorCode.FORBIDDEN.getStatus()).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Unexpected exception: {}", e.getMessage(), e);
    ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    return ResponseEntity.internalServerError().body(response);
  }
}
