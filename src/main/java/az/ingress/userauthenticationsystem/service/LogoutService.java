package az.ingress.userauthenticationsystem.service;

import az.ingress.userauthenticationsystem.dto.LogoutRequest;

public interface LogoutService {
    void logout(LogoutRequest request);
}
