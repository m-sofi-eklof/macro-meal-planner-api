package se.sofiekl.macromealplanner.dto.userAndLogin;

public record UserResponseDTO(
        Long id,
        String username,
        Double proteinGoal,
        Integer calorieGoal
) {
}
