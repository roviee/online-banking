package com.bank.online_banking.validator;

import com.bank.online_banking.dto.request.CreateAccountRequest;
import com.bank.online_banking.exceptions.TransactionProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class AccountValidator {

    public void validateCreateAccountRequest(CreateAccountRequest request) {
        
        if (request == null) {
            throw new TransactionProcessingException("Account creation request is null");
        }
        
        validateInitialDeposit(request.getInitialDeposit());
        validateCurrency(request.getCurrency());
        validateAccountType(request.getAccountType());
    }

    private void validateInitialDeposit(BigDecimal initialDeposit) {
        if (initialDeposit == null) {
            throw new TransactionProcessingException("Initial deposit is required");
        }
        
        if (initialDeposit.compareTo(BigDecimal.valueOf(500)) < 0) {
            throw new TransactionProcessingException("Minimum initial deposit is 500.00");
        }
        
        if (initialDeposit.scale() > 2) {
            throw new TransactionProcessingException("Amount cannot have more than 2 decimal places");
        }
        
        if (initialDeposit.signum() < 0) {
            throw new TransactionProcessingException("Initial deposit cannot be negative");
        }
        
        if (initialDeposit.compareTo(BigDecimal.valueOf(10_000_000)) > 0) {
            throw new TransactionProcessingException("Maximum initial deposit is 10,000,000");
        }
    }

    private void validateCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new TransactionProcessingException("Currency is required");
        }
        
        if (currency.length() != 3) {
            throw new TransactionProcessingException("Currency must be 3-letter ISO code");
        }
        
        if (!currency.matches("^[A-Z]{3}$")) {
            throw new TransactionProcessingException("Currency must be uppercase letters only");
        }
    }

    private void validateAccountType(Object accountType) {
        if (accountType == null) {
            throw new TransactionProcessingException("Account type is required");
        }
    }
}

