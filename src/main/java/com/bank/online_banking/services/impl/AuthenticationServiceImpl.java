package com.bank.online_banking.services.impl;

import com.bank.online_banking.core.mapper.UserMapper;
import com.bank.online_banking.dto.request.LoginRequest;
import com.bank.online_banking.dto.request.RegisterRequest;
import com.bank.online_banking.dto.response.LoginResponse;
import com.bank.online_banking.dto.response.RegisterResponse;
import com.bank.online_banking.model.entity.RefreshToken;
import com.bank.online_banking.model.entity.User;
import com.bank.online_banking.model.enums.UserRole;
import com.bank.online_banking.model.enums.UserStatus;
import com.bank.online_banking.repository.UserRepository;
import com.bank.online_banking.security.JwtService;
import com.bank.online_banking.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    @Override
    public RegisterResponse signup(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setUserRole(UserRole.ADMIN);
        user.setUserStatus(UserStatus.PENDING_VERIFICATION);

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getUserStatus()
        );

    }

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        String jwtToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserId());

        LoginResponse response = new LoginResponse();
        response.setAccessToken(jwtToken);
        response.setRefreshToken(refreshToken.getToken());
        response.setExpiresIn(jwtService.getExpirationTime());
        response.setUserDto(UserMapper.toDto(user));

        return response;
    }
}
