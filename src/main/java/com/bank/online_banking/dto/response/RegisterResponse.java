package com.bank.online_banking.dto.response;

import com.bank.online_banking.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private UUID userId;
    private String email;
    private UserStatus status;

}
