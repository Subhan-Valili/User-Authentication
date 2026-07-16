package az.ingress.userauthenticationsystem.controller;

import az.ingress.userauthenticationsystem.dto.AuthResponse;
import az.ingress.userauthenticationsystem.dto.RefreshTokenRequest;
import az.ingress.userauthenticationsystem.repository.redis.RefreshTokenRepository;
import az.ingress.userauthenticationsystem.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/refreshToken")
public class RefreshTokenController {
    private final RefreshTokenService service;

    @PostMapping
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }
}
