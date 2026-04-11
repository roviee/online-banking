package com.bank.online_banking.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a user already exists in the system.
 */
public class UserAlreadyExistsException extends BankingException {
    public UserAlreadyExistsException(String email) {
        super("User already registered with email: " + email, "USER_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}

