package alatoo.edu.kg.api.payload.user;

import alatoo.edu.kg.api.validation.AtLeastOne;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AtLeastOne
public class UserRegisterRequestDTO {
        @Size(min = 5, max = 16, message = "Username must be between 5 and 16 characters")
        String username;

        @NotBlank(message = "Password is empty")
        @Size(min = 7, max = 50, message = "Password must be longer, than 6 characters")
        String password;

        @Email(message = "Email is invalid")
        String email;
}
