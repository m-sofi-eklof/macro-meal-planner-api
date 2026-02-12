package se.sofiekl.macromealplanner.dto.userAndLogin;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Username cn not be empty")
        String username,

        @NotBlank(message = "Username can not be empty")
        String password
) {}
