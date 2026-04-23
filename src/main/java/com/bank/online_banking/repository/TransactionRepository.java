package com.bank.online_banking.repository;

import com.bank.online_banking.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Transaction findTopByOrderByIdDesc();
    @Query("""
        SELECT t FROM Transaction t
        WHERE t.id = :id
        AND (
            t.fromAccount.user.userId = :userId OR
            t.toAccount.user.userId = :userId
        )
    """)
    Optional<Transaction> findAuthorizedTransaction(UUID id, UUID userId);
}
