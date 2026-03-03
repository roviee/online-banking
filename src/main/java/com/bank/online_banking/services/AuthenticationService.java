package com.bank.online_banking.services;

import com.bank.online_banking.dto.request.LoginRequest;
import com.bank.online_banking.dto.request.RegisterRequest;
import com.bank.online_banking.dto.response.LoginResponse;
import com.bank.online_banking.dto.response.RegisterResponse;

public interface AuthenticationService {

    RegisterResponse signup(RegisterRequest request);
    LoginResponse authenticate(LoginRequest request);
}
