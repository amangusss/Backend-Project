package alatoo.edu.kg.api.validation;

import alatoo.edu.kg.api.payload.user.UserRegisterRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneValidator implements ConstraintValidator<AtLeastOne, UserRegisterRequestDTO> {

    @Override
    public boolean isValid(UserRegisterRequestDTO value, ConstraintValidatorContext context) {
        return (value.getUsername() != null && !value.getUsername().isBlank()) ||
                (value.getEmail() != null && !value.getEmail().isBlank());
    }
}
