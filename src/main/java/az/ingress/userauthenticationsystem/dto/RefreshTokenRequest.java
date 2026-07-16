package az.ingress.userauthenticationsystem.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull(message = "Token Bos ola bilmez!")
        String refreshTokenId
) {
}
