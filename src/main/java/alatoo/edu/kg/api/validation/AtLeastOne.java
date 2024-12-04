package alatoo.edu.kg.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
@Documented
@Constraint(validatedBy = AtLeastOneValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOne {
    String message() default "At least one of username or email must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
