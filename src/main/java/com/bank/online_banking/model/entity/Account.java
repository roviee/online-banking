package com.bank.online_banking.model.entity;

import com.bank.online_banking.model.enums.AccountStatus;
import com.bank.online_banking.model.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts", 
    indexes = {
        @Index(name = "idx_account_id", columnList = "account_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_account_status", columnList = "account_status")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "account_id", nullable = false, unique = true, length = 20)
    private String accountId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Initial deposit is required")
    @DecimalMin("0.00")
    private BigDecimal balance;

    @Column(nullable = false, precision = 19, scale = 2)
    @DecimalMin("0.00")
    private BigDecimal availableBalance;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "account_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(precision = 19, scale = 2)
    private BigDecimal minimumBalance;

    @Column(precision = 19, scale = 2)
    private BigDecimal overdraftLimit;

    @Column(precision = 19, scale = 2)
    private BigDecimal dailyTransferLimit;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime lastTransactionDate;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
