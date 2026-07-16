package az.ingress.userauthenticationsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "İstifadəçi adı boş ola bilməz!")
        @Size(min = 3, max = 20, message = "İstifadəçi adı 3-20 simvol arasında olmalıdır!")
        String username,

        @NotBlank(message = "Şifrə boş ola bilməz!")
        @Size(min = 6, message = "Şifrə ən azı 6 simvoldan ibarət olmalıdır!")
        String password,

        @NotBlank(message = "Email boş ola bilməz!")
        @Email(message = "Zəhmət olmasa, düzgün formatda bir email ünvanı daxil edin! (məs: misal@domain.com)")
        String email
) {}