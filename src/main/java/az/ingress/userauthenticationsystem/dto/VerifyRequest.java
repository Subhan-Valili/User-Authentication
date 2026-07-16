package az.ingress.userauthenticationsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyRequest(
        @NotBlank(message = "Email boş ola bilməz")
        @Email(message = "Düzgün email daxil edin")
        String email,

        @NotBlank(message = "OTP boş ola bilməz")
        String otp
) {
}
