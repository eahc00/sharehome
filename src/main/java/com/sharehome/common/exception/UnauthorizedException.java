package com.sharehome.common.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(UNAUTHORIZED, message);
    }
}
