package com.bank.online_banking.dto.request;

import com.bank.online_banking.dto.response.AccountDetailsResponse;
import com.bank.online_banking.model.entity.Account;
import com.bank.online_banking.model.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateTransactionRequest {
    private UUID fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String notes;
    private LocalDateTime scheduledDate;
}
