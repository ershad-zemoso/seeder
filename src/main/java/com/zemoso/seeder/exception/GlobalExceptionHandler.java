package com.zemoso.seeder.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> onNotFoundException(final NotFoundException ex) {

        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> onInternalServerError(final RuntimeException ex) {

        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>("Internal server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
