package com.bank.online_banking.validator;

import com.bank.online_banking.dto.request.CreateTransactionRequest;
import com.bank.online_banking.exceptions.TransactionProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class TransactionValidator {

    public void validateTransactionRequest(CreateTransactionRequest request) {
        
        if (request == null) {
            throw new TransactionProcessingException("Transaction request is null");
        }
        
        validateAmount(request.getAmount());
        validateDestinationAccount(request.getToAccountId());
        validateCurrency(request.getCurrency());
        validateDescription(request.getDescription());
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new TransactionProcessingException("Amount is required");
        }
        
        if (amount.signum() <= 0) {
            throw new TransactionProcessingException("Amount must be positive");
        }
        
        if (amount.scale() > 2) {
            throw new TransactionProcessingException("Amount cannot have more than 2 decimal places");
        }
        
        if (amount.compareTo(BigDecimal.valueOf(0.01)) < 0) {
            throw new TransactionProcessingException("Minimum transaction amount is 0.01");
        }
        
        if (amount.compareTo(BigDecimal.valueOf(1_000_000)) > 0) {
            throw new TransactionProcessingException("Maximum transaction amount is 1,000,000");
        }
    }

    private void validateDestinationAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new TransactionProcessingException("Destination account is required");
        }
        
        if (accountNumber.length() < 5 || accountNumber.length() > 20) {
            throw new TransactionProcessingException("Invalid account number format");
        }
    }

    private void validateCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new TransactionProcessingException("Currency is required");
        }
        
        if (currency.length() != 3) {
            throw new TransactionProcessingException("Currency must be 3-letter ISO code (e.g., USD, EUR)");
        }
        
        if (!currency.matches("^[A-Z]{3}$")) {
            throw new TransactionProcessingException("Currency must contain only uppercase letters");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new TransactionProcessingException("Description is required");
        }
        
        if (description.length() < 3 || description.length() > 200) {
            throw new TransactionProcessingException("Description must be 3-200 characters");
        }
    }
}

