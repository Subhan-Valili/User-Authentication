package az.ingress.userauthenticationsystem.service.impl;

import az.ingress.userauthenticationsystem.dto.*;
import az.ingress.userauthenticationsystem.entity.RefreshToken;
import az.ingress.userauthenticationsystem.entity.RoleEntity;
import az.ingress.userauthenticationsystem.entity.UserEntity;
import az.ingress.userauthenticationsystem.exception.exc.EmailExists;
import az.ingress.userauthenticationsystem.exception.exc.NotFound;
import az.ingress.userauthenticationsystem.exception.exc.RoleNotFound;
import az.ingress.userauthenticationsystem.exception.exc.UserAlreadyExists;
import az.ingress.userauthenticationsystem.repository.redis.RefreshTokenRepository;
import az.ingress.userauthenticationsystem.repository.jpa.RoleRepository;
import az.ingress.userauthenticationsystem.repository.jpa.UserRepository;
import az.ingress.userauthenticationsystem.security.JwtService;
import az.ingress.userauthenticationsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    public String register(RegisterRequest request) {
        log.info("Registration attempt initiated for username: {}", request.username());

        if (userRepository.findByUsername(request.username()).isPresent()) {
            log.warn("Registration failed. Username '{}' already exists", request.username());
            throw new UserAlreadyExists("İstifadəçi mövcuddur!");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            log.warn("Registration failed. Email '{}' already exists", request.email());
            throw new EmailExists("Email movcuddur!");
        }

        RoleEntity defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> {
                    log.error("Database Error: Crucial role 'ROLE_USER' not found!");
                    return new RoleNotFound("Xəta: Standart istifadəçi rolu tapılmadı!");
                });

        UserEntity userEntity = UserEntity.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .build();

        if (userEntity.getRoles() == null) {
            userEntity.setRoles(new LinkedHashSet<>());
        }
        userEntity.getRoles().add(defaultRole);

        userRepository.save(userEntity);
        log.info("User '{}' registered successfully with ROLE_USER", request.username());

        try {
            emailService.sendSimpleEmail(request.email(), "Registered To App", "Xos gelmisiniz!");
        } catch (Exception e) {
            log.error("Email sending failed for user '{}': {}", request.username(), e.getMessage());
        }

        return "User registered successfully";
    }

    @Override
    public String registerAdmin(RegisterRequest request) {
        log.info("Admin registration attempt initiated for username: {}", request.username());

        if (userRepository.findByUsername(request.username()).isPresent()) {
            log.warn("Admin registration failed. Username '{}' already exists", request.username());
            throw new UserAlreadyExists("Admin Movcuddur!");
        }

        RoleEntity defaultRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> {
                    log.error("Database Error: Crucial role 'ROLE_ADMIN' not found!");
                    return new RoleNotFound("Xəta: Standart istifadəçi rolu tapılmadı!");
                });

        UserEntity userEntity = UserEntity.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .build();

        if (userEntity.getRoles() == null) {
            userEntity.setRoles(new LinkedHashSet<>());
        }
        userEntity.getRoles().add(defaultRole);

        userRepository.save(userEntity);
        log.info("Admin '{}' registered successfully with ROLE_ADMIN", request.username());

        return "Admin registered successfully";
    }

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