package com.nookly.booking.exception.jwt;

public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String message) {
        super(message);
    }
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
