package com.sharehome.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<String> handleBadRequestException(BaseException e) {
        String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }
}
