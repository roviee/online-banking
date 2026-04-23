package com.bank.online_banking.services;

import com.bank.online_banking.dto.response.AccountCreateResponse;
import com.bank.online_banking.dto.request.CreateAccountRequest;
import com.bank.online_banking.dto.response.AccountSummaryResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AccountService {

    Page<AccountSummaryResponse> getAllAccounts();

    AccountSummaryResponse getAccountById(UUID accountId);

    AccountCreateResponse createAccount(CreateAccountRequest createAccountRequest);
}
