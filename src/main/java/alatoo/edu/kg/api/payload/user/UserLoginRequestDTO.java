package alatoo.edu.kg.api.payload.user;

import javax.validation.constraints.NotBlank;

public record UserLoginRequestDTO(
        @NotBlank(message = "Login is empty")
        String login,

        @NotBlank(message = "Password is empty")
        String password
){}
