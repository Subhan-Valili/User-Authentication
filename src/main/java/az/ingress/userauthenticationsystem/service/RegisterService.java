package az.ingress.userauthenticationsystem.service;

import az.ingress.userauthenticationsystem.dto.RegisterRequest;
import az.ingress.userauthenticationsystem.dto.VerifyRequest;

public interface RegisterService {
    String preRegisterUser(RegisterRequest request);
    void verifyAndRegisterUser(VerifyRequest request);
}