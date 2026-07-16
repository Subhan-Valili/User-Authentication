package az.ingress.userauthenticationsystem.dto;

import jakarta.validation.constraints.NotNull;

public record LogoutRequest(
        @NotNull(message = "Token Bos ola bilmez!")
        String refreshTokenId
) {
}
