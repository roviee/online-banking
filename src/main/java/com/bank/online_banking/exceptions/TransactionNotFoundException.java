package com.bank.online_banking.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends BankingException {
    public TransactionNotFoundException(String transactionIdentifier) {
        super("Transaction not found: " + transactionIdentifier, "TRANSACTION_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
