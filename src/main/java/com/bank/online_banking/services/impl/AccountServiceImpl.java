package com.bank.online_banking.services.impl;

import com.bank.online_banking.dto.response.AccountCreateResponse;
import com.bank.online_banking.core.mapper.AccountMapper;
import com.bank.online_banking.dto.request.CreateAccountRequest;
import com.bank.online_banking.dto.response.AccountDetailsResponse;
import com.bank.online_banking.dto.response.AccountSummaryResponse;
import com.bank.online_banking.model.entity.Account;
import com.bank.online_banking.model.entity.User;
import com.bank.online_banking.model.enums.AccountType;
import com.bank.online_banking.model.enums.UserStatus;
import com.bank.online_banking.repository.AccountRepository;
import com.bank.online_banking.repository.UserRepository;
import com.bank.online_banking.services.AccountService;
import com.bank.online_banking.core.generator.PrefixIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PrefixIdGenerator prefixIdGenerator;

    public List<AccountSummaryResponse> getAllAccounts(){
        return accountRepository.findAllAccounts();
    }

    public AccountDetailsResponse getAccountById(UUID accountId) {
        return accountRepository.findAccountById(accountId)
                .orElseThrow(() -> new IllegalStateException(
                        "Account not found with ID: " + accountId
                ));
    }

    @Override
    public AccountCreateResponse createAccount(CreateAccountRequest createAccountRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountNumber = prefixIdGenerator.generateAccountNumber();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(()-> new IllegalStateException("User Not Authenticated: " + email));

            Account account = new Account();
            account.setUser(user);
            account.setAccountNumber(accountNumber);
            account.setAccountType(AccountType.SAVINGS);
            account.setUserStatus(UserStatus.ACTIVE);
            account.setBalance(createAccountRequest.getInitialDeposit());
            account.setCurrency(createAccountRequest.getCurrency());

            return AccountMapper.toCreateResponse(accountRepository.save(account));
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }
}
