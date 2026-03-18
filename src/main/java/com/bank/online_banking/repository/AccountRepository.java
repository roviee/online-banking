package com.bank.online_banking.repository;

import com.bank.online_banking.dto.response.AccountDetailsResponse;
import com.bank.online_banking.dto.response.AccountSummaryResponse;
import com.bank.online_banking.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository <Account, UUID> {

    @Query("""
        SELECT new com.bank.online_banking.dto.response.AccountSummaryResponse(
            a.accountId,
            a.accountNumber,
            a.accountType,
            a.balance,
            a.currency,
            a.userStatus,
            a.createdAt
        )
        FROM Account a
    """)
    List<AccountSummaryResponse> findAllAccounts();

    @Query("""
        SELECT new com.bank.online_banking.dto.response.AccountDetailsResponse(
            a.accountId,
            a.accountNumber,
            a.accountType,
            a.balance,
            a.availableBalance,
            a.currency,
            a.userStatus,
            a.interestRate,
            a.minimumBalance,
            a.createdAt,
            a.lastTransactionDate
        )
        FROM Account a
        WHERE a.accountId = :accountId
    """)
    Optional<AccountDetailsResponse> findAccountById(@Param("accountId") UUID accountId);
    boolean existsByAccountNumber(String accountNumber);
}
