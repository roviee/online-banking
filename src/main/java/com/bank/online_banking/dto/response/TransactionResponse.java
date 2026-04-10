package com.bank.online_banking.dto.response;

import com.bank.online_banking.model.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponse(
        UUID transactionId,
        String referenceNumber,
        TransactionStatus transactionStatus,
        BigDecimal amount,
        LocalDate transactionDate,
        BigDecimal newBalance
) {}
