package se.sofiekl.macromealplanner.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FoodItemRequestValidator.class)
public @interface FoodItemRequestConstraint {
    String message() default "Invalid food item request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
