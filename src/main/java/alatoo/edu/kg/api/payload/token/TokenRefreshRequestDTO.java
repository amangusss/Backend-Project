package alatoo.edu.kg.api.payload.token;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequestDTO {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
