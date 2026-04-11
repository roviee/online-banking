package com.bank.online_banking.security;

import com.bank.online_banking.exceptions.UnauthorizedTransactionException;
import com.bank.online_banking.model.entity.User;
import com.bank.online_banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrentUserService {
    
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedTransactionException("User is not authenticated");
        }
        
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> {
                        log.error("Authenticated user not found in database: {}", userDetails.getUsername());
                        return new UnauthorizedTransactionException("User not found in system");
                    });
        }
        
        throw new UnauthorizedTransactionException("Invalid authentication principal");
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    public String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}

