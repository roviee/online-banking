package com.bank.online_banking.core.mapper;

import com.bank.online_banking.dto.UserDto;
import com.bank.online_banking.model.entity.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUserRole(user.getUserRole());
        return dto;
    }
}
