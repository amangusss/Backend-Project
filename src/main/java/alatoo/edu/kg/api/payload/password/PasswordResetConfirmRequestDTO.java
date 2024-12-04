package alatoo.edu.kg.api.payload.password;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetConfirmRequestDTO(
        @NotBlank(message = "Token is empty")
        String token,

        @NotBlank(message = "New password is empty")
        String newPassword
) {}