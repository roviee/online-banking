package com.bank.online_banking.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when user is not properly authenticated or authorized for an operation.
 */
public class UnauthorizedTransactionException extends BankingException {
    public UnauthorizedTransactionException(String message) {
        super(message, "UNAUTHORIZED_TRANSACTION", HttpStatus.FORBIDDEN);
    }
}

