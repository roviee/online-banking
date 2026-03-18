package com.bank.online_banking.dto.response;

import com.bank.online_banking.model.enums.AccountType;
import com.bank.online_banking.model.enums.UserStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountDetailsResponse(
        UUID accountId,
        String accountNumber,
        AccountType accountType,
        BigDecimal balance,
        BigDecimal availableBalance,
        String currency,
        UserStatus userStatus,
        BigDecimal interestRate,
        BigDecimal minimumBalance,
        LocalDateTime createdAt,
        LocalDateTime lastTransactionDate
) {}