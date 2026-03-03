package com.bank.online_banking.controllers;

import com.bank.online_banking.dto.ApiResponse;
import com.bank.online_banking.dto.request.LoginRequest;
import com.bank.online_banking.dto.request.RegisterRequest;
import com.bank.online_banking.dto.response.LoginResponse;
import com.bank.online_banking.dto.response.RegisterResponse;
import com.bank.online_banking.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = authenticationService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(new ApiResponse<>(true, null, response));
    }


}
