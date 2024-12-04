package alatoo.edu.kg.api.payload.password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequestDTO(
        @NotBlank(message = "Email is empty")
        @Email(message = "Email is invalid")
        String email
) {}
