package com.bank.online_banking.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a requested account is not found.
 */
public class AccountNotFoundException extends BankingException {
    public AccountNotFoundException(String accountIdentifier) {
        super("Account not found: " + accountIdentifier, "ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public AccountNotFoundException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.NOT_FOUND);
    }
}

