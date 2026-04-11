package com.bank.online_banking.services.impl;

import com.bank.online_banking.dto.response.AccountCreateResponse;
import com.bank.online_banking.core.mapper.AccountMapper;
import com.bank.online_banking.dto.request.CreateAccountRequest;
import com.bank.online_banking.dto.response.AccountSummaryResponse;
import com.bank.online_banking.exceptions.*;
import com.bank.online_banking.model.entity.Account;
import com.bank.online_banking.model.entity.User;
import com.bank.online_banking.model.enums.AccountStatus;
import com.bank.online_banking.repository.AccountRepository;
import com.bank.online_banking.services.AccountService;
import com.bank.online_banking.core.generator.PrefixIdGenerator;
import com.bank.online_banking.security.CurrentUserService;
import com.bank.online_banking.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    
    private final AccountRepository accountRepository;
    private final PrefixIdGenerator prefixIdGenerator;
    private final CurrentUserService currentUserService;
    private final AccountValidator validator;

    @Override
    public Page<AccountSummaryResponse> getAllAccounts() {
        return getAllAccounts(0, 20);
    }

    public Page<AccountSummaryResponse> getAllAccounts(int page, int size) {
        User currentUser = currentUserService.getCurrentUser();
        
        log.info("Fetching accounts for user: {} - page: {}, size: {}", 
                currentUser.getEmail(), page, size);
        
        Page<Account> accounts = accountRepository.findByUser(
                currentUser,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
        
        return accounts.map(AccountMapper::toSummaryResponse);
    }

    @Override
    public AccountSummaryResponse getAccountById(UUID accountId) {
        User currentUser = currentUserService.getCurrentUser();
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId.toString()));
        
        // Verify account ownership
        if (!account.getUser().getUserId().equals(currentUser.getUserId())) {
            log.warn("Unauthorized access attempt to account: {} by user: {}", 
                    accountId, currentUser.getEmail());
            throw new UnauthorizedTransactionException(
                    "You can only access your own accounts");
        }
        
        log.info("Account details retrieved: {} for user: {}", accountId, currentUser.getEmail());
        return AccountMapper.toDetailsResponse(account);
    }

    @Override
    @Transactional
    public AccountCreateResponse createAccount(CreateAccountRequest request) {
        
        User currentUser = currentUserService.getCurrentUser();
        
        log.info("Creating new account for user: {}", currentUser.getEmail());
        
        // Validate input
        validator.validateCreateAccountRequest(request);
        
        // Generate unique account number
        String accountNumber = prefixIdGenerator.generateAccountNumber();
        
        // Create account entity with sensible defaults
        Account account = Account.builder()
                .user(currentUser)
                .accountId(accountNumber)
                .accountType(request.getAccountType())
                .balance(request.getInitialDeposit())
                .availableBalance(request.getInitialDeposit())
                .currency(request.getCurrency())
                .accountStatus(AccountStatus.ACTIVE)
                .minimumBalance(BigDecimal.valueOf(500))
                .dailyTransferLimit(BigDecimal.valueOf(10000))
                .interestRate(BigDecimal.valueOf(2.5))
                .build();
        
        Account savedAccount = accountRepository.save(account);
        
        log.info("Account created successfully - ID: {}, Number: {}, for user: {}", 
                savedAccount.getId(), accountNumber, currentUser.getEmail());
        
        return AccountMapper.toCreateResponse(savedAccount);
    }
}
