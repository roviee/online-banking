package com.bank.online_banking.model.enums;

public enum UserStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    PENDING_VERIFICATION,
    LOCKED  // Account locked due to failed login attempts
}
