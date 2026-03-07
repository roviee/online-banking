package com.bank.online_banking.controllers;

import com.bank.online_banking.dto.ApiResponse;
import com.bank.online_banking.dto.request.LoginRequest;
import com.bank.online_banking.dto.request.RefreshTokenRequest;
import com.bank.online_banking.dto.request.RegisterRequest;
import com.bank.online_banking.dto.response.LoginResponse;
import com.bank.online_banking.dto.response.RefreshTokenResponse;
import com.bank.online_banking.dto.response.RegisterResponse;
import com.bank.online_banking.model.entity.User;
import com.bank.online_banking.services.AuthenticationService;
import com.bank.online_banking.services.impl.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = authenticationService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(new ApiResponse<>(true, null, response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        RefreshTokenResponse refreshTokenResponse = refreshTokenService.refreshAccessToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Token refreshed successfully", refreshTokenResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        refreshTokenService.deleteByUserId(user);
        return ResponseEntity.ok(new ApiResponse<>(
                true, "Logged out successfully", null));
    }

}
