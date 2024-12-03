package alatoo.edu.kg.api.service;

import alatoo.edu.kg.store.entity.RefreshToken;
import alatoo.edu.kg.store.entity.User;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUserId(Long userId);
}
