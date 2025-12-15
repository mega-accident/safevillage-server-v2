package com.safevillage.safevillage.global.error;

import com.safevillage.safevillage.global.dto.BaseResponse;
import com.safevillage.safevillage.global.exception.EmailAlreadyExistsException;
import com.safevillage.safevillage.global.exception.PhoneAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 유효하지 않은 인자
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse<String> handleIllegalArgument(IllegalArgumentException e) {
    return new BaseResponse<>(false, e.getMessage());
  }

  // 이미 존재하는 데이터
  @ExceptionHandler({PhoneAlreadyExistsException.class, EmailAlreadyExistsException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  public BaseResponse<String> handleConflict(RuntimeException e) {
    return new BaseResponse<>(false, e.getMessage());
  }

  // 유효성 검사 실패
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseResponse<String> handleValidationException(MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .reduce((msg1, msg2) -> msg1 + ", " + msg2)
        .orElse("유효하지 않은 입력입니다");

    return new BaseResponse<>(false, message);
  }

  // 그 외 모든 에러
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BaseResponse<String> handleAllException(Exception e) {
    log.error("unhandled exception", e);
    return new BaseResponse<>(false, "서버 내부 오류가 발생했습니다");
  }
}