package az.ingress.userauthenticationsystem.dto;

public record AuthResponse(
        String accessToken,
        String refreshTokenId
) {
}
