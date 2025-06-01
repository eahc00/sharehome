package com.sharehome.common.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(CONFLICT, message);
    }
}
