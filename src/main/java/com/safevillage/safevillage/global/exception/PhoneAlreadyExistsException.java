package com.safevillage.safevillage.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "이미 존재하는 전화번호입니다.")
public class PhoneAlreadyExistsException extends RuntimeException {

    // 생성자
    public PhoneAlreadyExistsException() {
        super("Phone number already exists.");
    }

    // 메시지 커스텀용 생성자
    public PhoneAlreadyExistsException(String message) {
        super(message);
    }
}