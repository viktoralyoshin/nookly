package com.nookly.booking.exception;

import com.nookly.booking.auth.exception.InvalidPasswordException;
import com.nookly.booking.auth.exception.UserExistsException;
import com.nookly.booking.exception.jwt.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserExistsException(UserExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("Error", e.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPasswordException(InvalidPasswordException e) {
        return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDataNotFoundException(DataNotFoundException e) {
        return ResponseEntity.badRequest().body(Map.of("Error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleJwtAuthenticationException(JwtAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error", e.getMessage()));
    }
}
