package com.example.gitmanager.util.exception;

import lombok.Getter;

@Getter
public class ExpiredRefreshTokenException extends RuntimeException {
    private final String refreshToken;

    public ExpiredRefreshTokenException(String refreshToken) {
        super("refresh token이 만료되었습니다.");
        this.refreshToken = refreshToken;
    }
}
