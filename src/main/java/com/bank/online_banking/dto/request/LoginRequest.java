package com.bank.online_banking.dto.request;

import lombok.*;

@Data
public class LoginRequest {
    private String email;

    private String password;
}
