package com.sharehome.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(BAD_REQUEST, message);
    }
}
