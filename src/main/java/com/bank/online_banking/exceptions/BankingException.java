package com.bank.online_banking.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Base exception for all banking domain exceptions.
 * Provides consistent error handling with error codes and HTTP status mapping.
 */
public abstract class BankingException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    public BankingException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public BankingException(String message, String errorCode, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}

