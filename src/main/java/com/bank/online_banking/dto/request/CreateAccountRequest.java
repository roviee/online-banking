package com.bank.online_banking.dto.request;

import com.bank.online_banking.model.enums.AccountType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    
    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Initial deposit is required")
    @DecimalMin(value = "500.00", message = "Minimum initial deposit is 500.00")
    @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
    private BigDecimal initialDeposit;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3-letter ISO code (e.g., USD)")
    private String currency;
}
