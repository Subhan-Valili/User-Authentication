package az.ingress.userauthenticationsystem.service.impl;

import az.ingress.userauthenticationsystem.dto.AuthResponse;
import az.ingress.userauthenticationsystem.dto.RefreshTokenRequest;
import az.ingress.userauthenticationsystem.entity.RefreshToken;
import az.ingress.userauthenticationsystem.exception.exc.NotFound;
import az.ingress.userauthenticationsystem.repository.jpa.RoleRepository;
import az.ingress.userauthenticationsystem.repository.jpa.UserRepository;
import az.ingress.userauthenticationsystem.repository.redis.RefreshTokenRepository;
import az.ingress.userauthenticationsystem.security.JwtService;
import az.ingress.userauthenticationsystem.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Token refresh process requested for Refresh Token ID: {}", request.refreshTokenId());

        RefreshToken token = refreshTokenRepository.findById(request.refreshTokenId())
                .orElseThrow(() -> {
                    log.warn("Token refresh failed. Refresh token ID '{}' not found or expired in Redis", request.refreshTokenId());
                    return new NotFound("Refresh token tapılmadı və ya vaxtı bitib!");
                });

        log.info("Valid refresh token found for user: {}", token.getUsername());

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUsername());

        String newAccessToken = jwtService.generateToken(userDetails);

        log.info("New access token generated during refresh for user: {}", token.getUsername());

        refreshTokenRepository.delete(token);

        log.info("Old refresh token ID '{}' removed from Redis", request.refreshTokenId());

        String newRefreshTokenId = UUID.randomUUID().toString();

        RefreshToken newRefreshToken = RefreshToken.builder()
                .id(newRefreshTokenId)
                .username(token.getUsername())
                .ttl(jwtService.getRefreshExpirationInSeconds())
                .build();

        refreshTokenRepository.save(newRefreshToken);

        log.info("New refresh token ID '{}' generated and rotation completed for user: {}", newRefreshTokenId, token.getUsername());

        return new AuthResponse(newAccessToken, newRefreshTokenId);
    }
}
