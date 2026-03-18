package com.bank.online_banking.dto.request;

import com.bank.online_banking.model.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    private AccountType accountType;

    @NotNull(message = "Initial deposit is required")
    @DecimalMin(value = "500.00")
    private BigDecimal initialDeposit;

    private String currency;
}
