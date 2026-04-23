package com.bank.online_banking.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when attempting to transfer from an account with insufficient balance.
 */
public class InsufficientBalanceException extends BankingException {
    public InsufficientBalanceException() {
        super("Insufficient account balance", "INSUFFICIENT_BALANCE", HttpStatus.BAD_REQUEST);
    }

    public InsufficientBalanceException(String message) {
        super(message, "INSUFFICIENT_BALANCE", HttpStatus.BAD_REQUEST);
    }
}

