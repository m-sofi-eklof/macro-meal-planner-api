package se.sofiekl.macromealplanner.dto.userAndLogin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Username can not be empty")
        @Size(min=3, max=30, message = "Username must be between 3 and 30 characters")
        String username,

        @NotBlank(message = "Password can not be empty")
        @Pattern(
                regexp = "^(?=.*\\d).{8,30}",
                message = "Password mst be between 8-30 characters and contain at least one number"
        )
        String password
) {}
