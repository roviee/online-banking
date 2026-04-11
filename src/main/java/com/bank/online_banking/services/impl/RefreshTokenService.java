package com.bank.online_banking.services.impl;

import com.bank.online_banking.dto.response.RefreshTokenResponse;
import com.bank.online_banking.exceptions.TransactionProcessingException;
import com.bank.online_banking.exceptions.UnauthorizedTransactionException;
import com.bank.online_banking.model.entity.RefreshToken;
import com.bank.online_banking.model.entity.User;
import com.bank.online_banking.repository.RefreshTokenRepository;
import com.bank.online_banking.repository.UserRepository;
import com.bank.online_banking.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${security.jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public RefreshToken createRefreshToken(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TransactionProcessingException("User not found"));

        // Delete existing refresh token for this user (ONE-TO-ONE relationship)
        refreshTokenRepository.findByUser(user)
                .ifPresent(existingToken -> {
                    log.debug("Deleting existing refresh token for user: {}", userId);
                    refreshTokenRepository.delete(existingToken);
                });

        // Create new refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        refreshToken.setToken(UUID.randomUUID().toString());

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        log.info("New refresh token created for user: {}", userId);

        return savedToken;
    }

    @Transactional
    public RefreshTokenResponse refreshAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> {
                    log.warn("Refresh token not found: {}", refreshToken);
                    return new UnauthorizedTransactionException("Refresh token not found");
                });

        if (isTokenExpired(token)) {
            refreshTokenRepository.delete(token);
            log.warn("Refresh token expired for user: {}", token.getUser().getUserId());
            throw new UnauthorizedTransactionException("Refresh token expired");
        }

        User user = token.getUser();

        if (!user.isEnabled()) {
            log.warn("User disabled, cannot refresh token: {}", user.getUserId());
            throw new UnauthorizedTransactionException("User account is disabled");
        }

        // Delete old refresh token
        refreshTokenRepository.delete(token);
        log.debug("Old refresh token deleted for user: {}", user.getUserId());

        // Create new refresh token
        RefreshToken newRefreshToken = createRefreshToken(user.getUserId());

        // Generate new access token
        String newAccessToken = jwtService.generateToken(user);

        log.info("Access token refreshed for user: {}", user.getUserId());

        return new RefreshTokenResponse(newAccessToken, newRefreshToken.getToken(), refreshTokenExpiration);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    @Transactional
    public void deleteByUserId(User user) {
        try {
            refreshTokenRepository.deleteByUser(user);
            log.info("Refresh token deleted for user: {}", user.getUserId());
        } catch (Exception e) {
            log.error("Error deleting refresh token for user: {}", user.getUserId(), e);
            // Don't throw exception here as logout should succeed regardless
        }
    }

}
