package com.bank.online_banking.core.generator;

import com.bank.online_banking.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class PrefixIdGenerator {
    private static final String PREFIX = "FS";

    private final AccountRepository accountRepository;
    private final SecureRandom random = new SecureRandom();

    public String generateAccountNumber() {
        String accountNumber;

        do {
            int randomNumber = random.nextInt(100000000);
            accountNumber = PREFIX + String.format("%08d", randomNumber);
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
}