package com.bank.online_banking.dto.request;

import com.bank.online_banking.model.enums.UserRole;
import com.bank.online_banking.model.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String email;
    private String password;

    private String firstName;
    private String lastName;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private UserRole userRole;
    private UserStatus userStatus;
}
