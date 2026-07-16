package az.ingress.userauthenticationsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonInclude;

public record LoginRequest(

        @NotBlank(message = "İstifadəçi adı boş ola bilməz!")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String username,

        @NotBlank(message = "Şifrə boş ola bilməz!")
        @Size(min = 6, message = "Şifrə ən azı 6 simvoldan ibarət olmalıdır!")
        String password

) {
}