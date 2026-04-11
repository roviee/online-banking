package com.bank.online_banking.core.mapper;

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
}
