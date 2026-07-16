package az.ingress.userauthenticationsystem.service;

import az.ingress.userauthenticationsystem.dto.*;

public interface AuthService {
    String register(RegisterRequest request);
    String registerAdmin(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(LogoutRequest request);
}
