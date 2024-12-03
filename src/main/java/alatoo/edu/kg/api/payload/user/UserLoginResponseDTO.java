package alatoo.edu.kg.api.payload.user;

import alatoo.edu.kg.store.enums.user.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {
    private String username;
    private Set<Roles> roles;
    private String accessToken;
    private String refreshToken;
}
