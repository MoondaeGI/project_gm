package com.example.gitmanager.util.exception;

public class ExpiredRefreshTokenException extends RuntimeException {
    public ExpiredRefreshTokenException(String message) {
        super(message);
    }
    public ExpiredRefreshTokenException() { super("refresh token이 만료되었습니다."); }
}
