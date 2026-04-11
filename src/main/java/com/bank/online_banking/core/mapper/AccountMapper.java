package com.bank.online_banking.core.mapper;

import com.bank.online_banking.dto.response.AccountCreateResponse;
import com.bank.online_banking.dto.response.AccountSummaryResponse;
import com.bank.online_banking.model.entity.Account;

public class AccountMapper {

    public static AccountSummaryResponse toDetailsResponse(Account acc) {
        return new AccountSummaryResponse(
                acc.getAccountId(),
                acc.getAccountType(),
                acc.getBalance(),
                acc.getCurrency(),
                acc.getAccountStatus(),
                acc.getCreatedAt()
        );
    }

    public static AccountCreateResponse toCreateResponse(Account account) {
        AccountCreateResponse dto = new AccountCreateResponse();
        dto.setAccountId(account.getAccountId());
        dto.setAccountType(account.getAccountType().toString());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());
        dto.setAccountStatus(account.getAccountStatus().toString());
        return dto;
    }

    public static AccountSummaryResponse toSummaryResponse(Account account) {
        return new AccountSummaryResponse(
                account.getAccountId(),
                account.getAccountType(),
                account.getBalance(),
                account.getCurrency(),
                account.getAccountStatus(),
                account.getCreatedAt()
        );
    }
}
