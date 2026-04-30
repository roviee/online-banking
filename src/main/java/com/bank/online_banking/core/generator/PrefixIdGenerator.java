package com.bank.online_banking.core.generator;

import com.bank.online_banking.model.entity.Transaction;
import com.bank.online_banking.repository.AccountRepository;
import com.bank.online_banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class PrefixIdGenerator {
    private static final String PREFIX = "FS";
    private static final String TXN = "TXN";

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final SecureRandom random = new SecureRandom();

    public String generateAccountNumber() {
        String accountId;

        do {
            int randomNumber = random.nextInt(100000000);
            accountId = PREFIX + String.format("%08d", randomNumber);
        } while (accountRepository.existsByAccountId(accountId));

        return accountId;
    }

    public String generateTransactionNumber(){
        Long seq = transactionRepository.getNextTxnSequence();
        return TXN + String.format("%07d", seq);
    }

}