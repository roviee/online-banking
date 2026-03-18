package com.bank.online_banking.core.mapper;

import com.bank.online_banking.dto.response.AccountCreateResponse;
import com.bank.online_banking.dto.response.AccountSummaryResponse;
import com.bank.online_banking.model.entity.Account;

public class AccountMapper {

    public static AccountSummaryResponse toDetailsResponse(Account acc) {
        return new AccountSummaryResponse(
                acc.getAccountId(),
                acc.getAccountNumber(),
                acc.getAccountType(),
                acc.getBalance(),
                acc.getCurrency(),
                acc.getUserStatus(),
                acc.getCreatedAt()
        );
    }

    public static AccountCreateResponse toCreateResponse(Account account) {
        AccountCreateResponse dto = new AccountCreateResponse();
        dto.setAccountId(account.getAccountId().toString());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType().toString());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());
        dto.setUserStatus(account.getUserStatus().toString());

        return dto;
    }
}
