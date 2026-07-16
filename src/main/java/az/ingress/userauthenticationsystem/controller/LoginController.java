package az.ingress.userauthenticationsystem.controller;

import az.ingress.userauthenticationsystem.dto.AuthResponse;
import az.ingress.userauthenticationsystem.dto.LoginRequest;
import az.ingress.userauthenticationsystem.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/login")
public class LoginController {
    private final LoginService service;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }
}
