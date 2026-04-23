package com.bank.online_banking.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Thrown when transaction processing fails due to business logic validation.
 */
public class TransactionProcessingException extends BankingException {
    public TransactionProcessingException(String message) {
        super(message, "TRANSACTION_FAILED", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

