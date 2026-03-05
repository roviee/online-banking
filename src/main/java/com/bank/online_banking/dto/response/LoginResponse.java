package com.bank.online_banking.dto.response;

import com.bank.online_banking.dto.UserDto;
import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn;

    private UserDto userDto;

}
