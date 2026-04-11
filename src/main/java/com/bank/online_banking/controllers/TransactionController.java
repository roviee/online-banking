package com.bank.online_banking.controllers;

import com.bank.online_banking.dto.ApiResponse;
import com.bank.online_banking.dto.request.CreateAccountRequest;
import com.bank.online_banking.dto.request.CreateTransactionRequest;
import com.bank.online_banking.dto.response.AccountCreateResponse;
import com.bank.online_banking.dto.response.TransactionResponse;
import com.bank.online_banking.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse> createAccounts(@Valid @RequestBody CreateTransactionRequest createTransactionRequest) {
        TransactionResponse transactionResponse = transactionService.createTransaction(createTransactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Transaction created successfully", transactionResponse));
    }
}
