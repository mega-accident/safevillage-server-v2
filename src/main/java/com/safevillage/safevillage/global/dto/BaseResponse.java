package com.safevillage.safevillage.global.dto;

public record BaseResponse<T>(
    boolean success,
    T data
) {
  public static <T> BaseResponse<T> ok(T data) {
    return new BaseResponse<T>(true, data);
  }
  // 데이터 없는 성공
  public static BaseResponse<Void> ok() {
    return new BaseResponse<Void>(true, null);
  }
}