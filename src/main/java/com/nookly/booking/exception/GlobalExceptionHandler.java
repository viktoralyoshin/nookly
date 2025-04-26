package com.nookly.booking.exception;

import com.nookly.booking.auth.exception.UserExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserExistsException(UserExistsException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
