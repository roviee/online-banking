package com.bank.online_banking.model.entity;

import com.bank.online_banking.model.enums.AccountType;
import com.bank.online_banking.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private UUID accountId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @NotNull(message = "Initial deposit is required")
    @DecimalMin(value = "500.00")
    private BigDecimal balance;

    private BigDecimal availableBalance;

    private String currency;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private BigDecimal interestRate;

    private BigDecimal minimumBalance;

    private BigDecimal overdraftLimit;

    private BigDecimal dailyTransferLimit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastTransactionDate;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
