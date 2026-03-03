package com.bank.online_banking.dto;

import com.bank.online_banking.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole userRole;

}
