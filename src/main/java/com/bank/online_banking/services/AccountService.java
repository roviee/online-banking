package com.bank.online_banking.services;

import com.bank.online_banking.dto.response.AccountCreateResponse;
import com.bank.online_banking.dto.request.CreateAccountRequest;
import com.bank.online_banking.dto.response.AccountDetailsResponse;
import com.bank.online_banking.dto.response.AccountSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountSummaryResponse> getAllAccounts();
    AccountDetailsResponse getAccountById(UUID accountId);
    AccountCreateResponse createAccount(CreateAccountRequest createAccountRequest);
}
