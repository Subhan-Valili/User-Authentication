package az.ingress.userauthenticationsystem.service;

import az.ingress.userauthenticationsystem.dto.AuthResponse;
import az.ingress.userauthenticationsystem.dto.LoginRequest;

public interface LoginService {
    AuthResponse login(LoginRequest request);
}
