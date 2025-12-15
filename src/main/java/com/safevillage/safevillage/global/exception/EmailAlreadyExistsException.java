package com.safevillage.safevillage.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "이미 존재하는 이메일입니다.")
public class EmailAlreadyExistsException extends RuntimeException {

    // 생성자
    public EmailAlreadyExistsException() {
        super("Email already exists.");
    }

    // 메시지 커스텀용 생성자
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}