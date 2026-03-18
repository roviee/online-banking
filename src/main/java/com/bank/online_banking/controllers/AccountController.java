package com.bank.online_banking.controllers;

import com.bank.online_banking.dto.response.AccountCreateResponse;
import com.bank.online_banking.dto.ApiResponse;
import com.bank.online_banking.dto.request.CreateAccountRequest;
import com.bank.online_banking.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAccounts() {
        return ResponseEntity.ok(new ApiResponse<>(true, null, accountService.getAllAccounts()));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse> getAccountById(@PathVariable UUID accountId) {
        return ResponseEntity.ok(new ApiResponse<>(true, null, accountService.getAccountById(accountId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAccounts(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        AccountCreateResponse account = accountService.createAccount(createAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Account created successfully", account));
    }
}
