package com.bank.online_banking.dto.response;

import com.bank.online_banking.model.enums.AccountType;
import com.bank.online_banking.model.enums.UserStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountSummaryResponse(
        UUID accountId,
        String accountNumber,
        AccountType accountType,
        BigDecimal balance,
        String currency,
        UserStatus userStatus,
        LocalDateTime createdAt
) {}
