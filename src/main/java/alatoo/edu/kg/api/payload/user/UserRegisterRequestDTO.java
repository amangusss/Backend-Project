package alatoo.edu.kg.api.payload.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record UserRegisterRequestDTO(
        @NotBlank(message = "Username is empty")
        String username,

        @NotBlank(message = "Password is empty")
        String password,

        @NotBlank(message = "Email is empty")
        @Email(message = "Email is invalid")
        String email
) {}
