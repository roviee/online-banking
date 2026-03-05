package com.bank.online_banking.services.impl;

import com.bank.online_banking.dto.response.RefreshTokenResponse;
import com.bank.online_banking.model.entity.RefreshToken;
import com.bank.online_banking.model.entity.User;
import com.bank.online_banking.repository.RefreshTokenRepository;
import com.bank.online_banking.repository.UserRepository;
import com.bank.online_banking.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${security.jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    public RefreshToken createRefreshToken(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshTokenResponse refreshAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (isTokenExpired(token)) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        User user = token.getUser();

        if (!user.isEnabled()) {
            throw new RuntimeException("User disabled");
        }


        refreshTokenRepository.delete(token);

        RefreshToken newRefreshToken = createRefreshToken(user.getUserId());

        String newAccessToken = jwtService.generateToken(token.getUser());

        return new RefreshTokenResponse(newAccessToken, newRefreshToken.getToken(), refreshTokenExpiration);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    @Transactional
    public void deleteByUserId(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

}
