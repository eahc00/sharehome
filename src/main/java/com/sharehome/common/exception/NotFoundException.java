package com.sharehome.common.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
