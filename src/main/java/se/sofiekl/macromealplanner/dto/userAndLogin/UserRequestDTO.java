package se.sofiekl.macromealplanner.dto.userAndLogin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "Username can not be empty")
        @Size(min=5, max=30, message = "Username must be between 5-30 characters")
        String username,

        @NotBlank(message = "Password can not be empty")
        @Pattern(
                regexp = "^(?=.*\\d).{8,30}",
                message = "Password mst be between 8-30 characters and contain at least one number"
        )
        String password,

        @Positive(message = "Protein goal must be a positive value")
        Double proteinGoal,

        @Positive(message = "Calorie goal must be a positive value")
        Integer calorieGoal
) {
}
