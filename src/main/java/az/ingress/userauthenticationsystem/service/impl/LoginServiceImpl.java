package az.ingress.userauthenticationsystem.service.impl;

import az.ingress.userauthenticationsystem.dto.AuthResponse;
import az.ingress.userauthenticationsystem.dto.LoginRequest;
import az.ingress.userauthenticationsystem.entity.RefreshToken;
import az.ingress.userauthenticationsystem.repository.redis.RefreshTokenRepository;
import az.ingress.userauthenticationsystem.security.JwtService;
import az.ingress.userauthenticationsystem.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login authentication attempt started for user: {}", request.username());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        log.info("User '{}' successfully authenticated by AuthenticationManager", request.username());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        String accessToken = jwtService.generateToken(userDetails);

        log.info("Access token successfully generated for user: {}", request.username());

        refreshTokenRepository.findByUsername(request.username())
                .ifPresent(existingToken -> {
                    log.info("Old refresh token found for user '{}'. Deleting from Redis...", request.username());
                    refreshTokenRepository.delete(existingToken);
                });

        String refreshTokenId = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .id(refreshTokenId)
                .username(request.username())
                .ttl(jwtService.getRefreshExpirationInSeconds())
                .build();

        refreshTokenRepository.save(refreshToken);

        log.info("New refresh token successfully generated and stored in Redis for user: {}", request.username());

        return new AuthResponse(accessToken, refreshTokenId);
    }
}
