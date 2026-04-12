package com.bank.online_banking.services.impl;

import com.bank.online_banking.core.mapper.UserMapper;
import com.bank.online_banking.dto.request.LoginRequest;
import com.bank.online_banking.dto.request.RegisterRequest;
import com.bank.online_banking.dto.response.LoginResponse;
import com.bank.online_banking.dto.response.RegisterResponse;
import com.bank.online_banking.exceptions.TransactionProcessingException;
import com.bank.online_banking.exceptions.UnauthorizedTransactionException;
import com.bank.online_banking.exceptions.UserAlreadyExistsException;
import com.bank.online_banking.model.entity.RefreshToken;
import com.bank.online_banking.model.entity.User;
import com.bank.online_banking.model.enums.UserRole;
import com.bank.online_banking.model.enums.UserStatus;
import com.bank.online_banking.repository.UserRepository;
import com.bank.online_banking.security.JwtService;
import com.bank.online_banking.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long ACCOUNT_LOCK_DURATION_HOURS = 1;

    @Override
    @Transactional
    public RegisterResponse signup(RegisterRequest request) {
        
        log.info("Processing signup for email: {}", request.getEmail());
        
        // Validate email format
        if (!isValidEmail(request.getEmail())) {
            throw new TransactionProcessingException("Invalid email format");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Signup attempted with existing email: {}", request.getEmail());
            throw new UserAlreadyExistsException(request.getEmail());
        }
        
        // Validate password strength
        validatePasswordStrength(request.getPassword());
        
        // Create new user with CUSTOMER role (NOT ADMIN!)
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .userRole(UserRole.CUSTOMER)  // Default role
                .userStatus(UserStatus.ACTIVE)
                .failedLoginAttempts(0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        
        User savedUser = userRepository.save(user);
        
        log.info("User registered successfully: {}", savedUser.getUserId());
        
        return new RegisterResponse(
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getUserStatus()
        );
    }

    @Override
    @Transactional
    public LoginResponse authenticate(LoginRequest request) {
        
        log.info("Login attempt for email: {}", request.getEmail());
        
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for email: {}", request.getEmail());
            handleFailedLoginAttempt(request.getEmail());
            throw new UnauthorizedTransactionException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedTransactionException("User not found"));
        
        // Check account status
        if (user.getUserStatus() == UserStatus.LOCKED) {
            if (user.getAccountLockedUntil() != null && Instant.now().isBefore(user.getAccountLockedUntil())) {
                throw new UnauthorizedTransactionException(
                        "Account is locked. Please try again after some time.");
            } else {
                // Unlock account if lock duration has expired
                user.setUserStatus(UserStatus.ACTIVE);
                user.setFailedLoginAttempts(0);
            }
        }
        
        // Reset failed attempts on successful login
        user.setFailedLoginAttempts(0);
        user.setLastLogin(Instant.now());
        userRepository.save(user);
        
        log.debug("Generating JWT token for user: {}", user.getUserId());

        // Generate tokens
        String jwtToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserId());

        log.info("User logged in successfully: {}", user.getUserId());
        
        return LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(jwtService.getExpirationTime())
                .tokenType("Bearer")
                .userDto(UserMapper.toDto(user))
                .build();
    }

    /**
     * Handle failed login attempt and lock account if needed.
     */
    private void handleFailedLoginAttempt(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            
            // Lock account after MAX_FAILED_ATTEMPTS
            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.setUserStatus(UserStatus.LOCKED);
                user.setAccountLockedUntil(Instant.now().plus(Duration.ofHours(ACCOUNT_LOCK_DURATION_HOURS)));
                log.warn("Account locked due to multiple failed attempts: {}", email);
            }
            
            userRepository.save(user);
        }
    }

    /**
     * Validate password strength requirements.
     */
    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new TransactionProcessingException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new TransactionProcessingException(
                    "Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new TransactionProcessingException(
                    "Password must contain at least one digit");
        }
        if (!password.matches(".*[!@#$%^&*()_\\-+=\\[\\]{};:'\",.<>?/\\\\|`~].*")) {
            throw new TransactionProcessingException(
                    "Password must contain at least one special character");
        }
    }

    /**
     * Validate email format.
     */
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}
