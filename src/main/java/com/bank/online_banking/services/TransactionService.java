package com.bank.online_banking.services;

import com.bank.online_banking.dto.request.CreateTransactionRequest;
import com.bank.online_banking.dto.response.TransactionDetailsResponse;
import com.bank.online_banking.dto.response.TransactionResponse;
import com.bank.online_banking.model.entity.Transaction;

import java.util.UUID;

public interface TransactionService {
    TransactionDetailsResponse getTransactionById(UUID transactionId);
    TransactionResponse createTransaction(CreateTransactionRequest transaction);
}
