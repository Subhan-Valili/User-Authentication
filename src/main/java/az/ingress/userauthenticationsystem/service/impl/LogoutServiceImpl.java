package az.ingress.userauthenticationsystem.service.impl;

import az.ingress.userauthenticationsystem.dto.LogoutRequest;
import az.ingress.userauthenticationsystem.repository.redis.RefreshTokenRepository;
import az.ingress.userauthenticationsystem.service.LogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutServiceImpl implements LogoutService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(LogoutRequest request) {
        log.info("Logout process requested for Refresh Token ID: {}", request.refreshTokenId());
        refreshTokenRepository.findById(request.refreshTokenId())
                .ifPresentOrElse(
                        token -> {
                            refreshTokenRepository.delete(token);
                            log.info("User '{}' logged out successfully. Refresh token ID '{}' removed from Redis.",
                                    token.getUsername(), request.refreshTokenId());
                        },
                        () -> log.warn("Logout attempt failed. Refresh token ID '{}' not found or already expired.",
                                request.refreshTokenId())
                );
    }
}
