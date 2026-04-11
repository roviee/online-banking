package com.bank.online_banking.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateTransactionRequest {
    
    @NotBlank(message = "Destination account is required")
    @Size(min = 5, max = 20, message = "Invalid account number format")
    private String toAccountId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;
    
    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3-letter ISO code")
    private String currency;
    
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 200, message = "Description must be 3-200 characters")
    private String description;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
    
    private LocalDateTime scheduledDate;
}
