package com.bank.online_banking.services;

import com.bank.online_banking.dto.request.CreateTransactionRequest;
import com.bank.online_banking.dto.response.TransactionResponse;
import com.bank.online_banking.model.entity.Transaction;

public interface TransactionService {
    TransactionResponse createTransaction(CreateTransactionRequest transaction);
}
