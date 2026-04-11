package com.bank.online_banking.repository;

import com.bank.online_banking.dto.response.AccountDetailsResponse;
import com.bank.online_banking.dto.response.AccountSummaryResponse;
import com.bank.online_banking.model.entity.Account;
import com.bank.online_banking.model.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByAccountId(String accountId);

    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByAccountIdWithLock(@Param("accountId") String accountId);

    boolean existsByAccountId(String accountId);

    Optional<Account> findByUserEmail(String email);

    Page<Account> findByUser(User user, Pageable pageable);

    List<Account> findByUser(User user);

    boolean existsByUser(User user);
}
