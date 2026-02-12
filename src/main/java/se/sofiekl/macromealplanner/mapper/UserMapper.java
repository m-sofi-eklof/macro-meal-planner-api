package se.sofiekl.macromealplanner.mapper;

import se.sofiekl.macromealplanner.dto.userAndLogin.UserRequestDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.UserResponseDTO;
import se.sofiekl.macromealplanner.model.User;

public class UserMapper {

    /**
     * Maps a User  object to a Response Data Transfer Object
     * @param user The User object
     * @return The response data as DTO
     */
    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getProteinGoal(),
                user.getCalorieGoal()
        );
    }

    /**
     * Maps a Request Data Transfer Object to a User object
     * @param request The request data as DTO
     * @return The User object
     */
    public static User fromDTO(UserRequestDTO request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setProteinGoal(request.proteinGoal());
        user.setCalorieGoal(request.calorieGoal());
        return user;
    }
}
