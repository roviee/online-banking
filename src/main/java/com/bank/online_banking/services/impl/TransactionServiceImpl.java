package com.bank.online_banking.services.impl;

import com.bank.online_banking.dto.request.CreateTransactionRequest;
import com.bank.online_banking.dto.response.TransactionResponse;
import com.bank.online_banking.repository.AccountRepository;
import com.bank.online_banking.repository.TransactionRepository;
import com.bank.online_banking.services.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest transaction) {
        return null;
    }
}
