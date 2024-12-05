package alatoo.edu.kg.api.service.impl;


import alatoo.edu.kg.api.exception.NotFoundException;
import alatoo.edu.kg.api.exception.TokenRefreshException;
import alatoo.edu.kg.api.service.RefreshTokenService;
import alatoo.edu.kg.store.entity.RefreshToken;
import alatoo.edu.kg.store.entity.User;
import alatoo.edu.kg.store.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpirationMillis;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.from(LocalDateTime.now().plusSeconds(refreshExpirationMillis / 1000)))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken());
        }

        return token;
    }

    @Override
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    public void delete(String token) {
        logger.info("Deleting refresh token: {}", token);
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    logger.warn("Refresh token not found: {}", token);
                    return new NotFoundException("Refresh token not found");
                });
        refreshTokenRepository.delete(refreshToken);
        logger.info("Refresh token {} deleted successfully.", token);
    }
}
