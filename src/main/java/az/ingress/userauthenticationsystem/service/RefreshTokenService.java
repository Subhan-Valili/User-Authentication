package az.ingress.userauthenticationsystem.service;

import az.ingress.userauthenticationsystem.dto.AuthResponse;
import az.ingress.userauthenticationsystem.dto.RefreshTokenRequest;

public interface RefreshTokenService {
    AuthResponse refreshToken(RefreshTokenRequest request);
}
