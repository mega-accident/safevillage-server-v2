package com.safevillage.safevillage.globlal.handler;

import com.safevillage.safevillage.globlal.dto.ErrorResponse;
import com.safevillage.safevillage.globlal.exception.EmailAlreadyExistsException;
import com.safevillage.safevillage.globlal.exception.PhoneAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(PhoneAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handlePhoneAlreadyExistsException(PhoneAlreadyExistsException e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .reduce((msg1, msg2) -> msg1 + ", " + msg2)
        .orElse("유효하지 않은 입력입니다");
    ErrorResponse errorResponse = new ErrorResponse(message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Unhandled exception", e);
    ErrorResponse errorResponse = new ErrorResponse("서버 오류가 발생했습니다");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
