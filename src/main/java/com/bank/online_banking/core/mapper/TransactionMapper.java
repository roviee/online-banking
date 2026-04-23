package com.bank.online_banking.core.mapper;

import com.bank.online_banking.dto.AccountDto;
import com.bank.online_banking.dto.response.TransactionDetailsResponse;
import com.bank.online_banking.dto.response.TransactionResponse;
import com.bank.online_banking.model.entity.Transaction;

public class TransactionMapper {
    public static TransactionResponse toCreateResponse(Transaction transaction) {
        TransactionResponse dto = new TransactionResponse();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setReferenceNumber(transaction.getReferenceNumber());
        dto.setTransactionStatus(transaction.getTransactionStatus());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionDate(transaction.getExecutionDate());
        dto.setNewBalance(transaction.getBalanceAfter());

        return dto;

    }

    public static TransactionDetailsResponse toDetailsResponse(Transaction transaction) {
        return new TransactionDetailsResponse(
                transaction.getTransactionId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionStatus(),
                transaction.getDescription(),
                new AccountDto(
                        transaction.getFromAccount().getAccountId(),
                        transaction.getFromAccount().getUser().getUsername()
                ),
                new AccountDto(
                        transaction.getToAccount().getAccountId(),
                        transaction.getToAccount().getUser().getUsername()
                ),
                transaction.getExecutionDate(),
                transaction.getReferenceNumber(),
                transaction.getNotes()
        );

    }
}
