package com.example.gitmanager.util.exception;

public class UnAuthenticationException extends RuntimeException {
    public UnAuthenticationException(String message) {
        super(message);
    }
    public UnAuthenticationException() { super("해당 서비스에 접근 권한이 없습니다.");}
}
