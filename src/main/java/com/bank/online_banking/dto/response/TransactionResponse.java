package com.bank.online_banking.dto.response;

import com.bank.online_banking.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private String transactionId;
    private String referenceNumber;
    private TransactionStatus transactionStatus;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private BigDecimal newBalance;
}
