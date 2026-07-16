package az.ingress.userauthenticationsystem.controller;

import az.ingress.userauthenticationsystem.dto.LogoutRequest;
import az.ingress.userauthenticationsystem.service.LogoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/logout")
public class LogoutController {
    private final LogoutService service;

    @PostMapping
    public void logout(@RequestBody @Valid LogoutRequest request) {
        service.logout(request);
    }
}
