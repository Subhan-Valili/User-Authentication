package az.ingress.userauthenticationsystem.controller;

import az.ingress.userauthenticationsystem.dto.RegisterRequest;
import az.ingress.userauthenticationsystem.dto.VerifyRequest;
import az.ingress.userauthenticationsystem.service.RegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/register")
public class RegisterController {

    private final RegisterService service;

    @PostMapping("/pre")
    public String preRegister(@Valid @RequestBody RegisterRequest request) {
        return service.preRegisterUser(request);
    }

    @PostMapping("/verify")
    public void verifyAndRegister(@Valid @RequestBody VerifyRequest request){
        service.verifyAndRegisterUser(request);
    }

}
