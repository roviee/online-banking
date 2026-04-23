package com.bank.online_banking.dto.response;

import com.bank.online_banking.dto.AccountDto;
import com.bank.online_banking.model.entity.Account;
import com.bank.online_banking.model.enums.TransactionStatus;
import com.bank.online_banking.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDetailsResponse(
        String transactionId,
        TransactionType transactionType,
        BigDecimal amount,
        String currency,
        TransactionStatus transactionStatus,
        String description,
        AccountDto fromAccount,
        AccountDto toAccount,
        LocalDateTime transactionDate,
        String referenceNumber,
        String notes
) {}
