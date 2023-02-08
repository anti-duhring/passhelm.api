package com.passhelm.passhelm.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handle404Error(EntityNotFoundException exception) {
        return ResponseEntity.status(404).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity handle400Error(IllegalStateException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
