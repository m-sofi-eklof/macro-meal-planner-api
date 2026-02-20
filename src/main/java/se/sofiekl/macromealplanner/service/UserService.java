package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.MacroGoalsDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.AuthResponseDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.UserRequestDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.UserResponseDTO;
import se.sofiekl.macromealplanner.mapper.UserMapper;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,  JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Create a new User
     * @param username
     * @param rawPassword
     * @return
     */
    public User createUser(String username, String rawPassword) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    /**
     * Update a User (password and username only)
     * @param request The requestData of the user
     * @return
     */
    @Transactional
    public AuthResponseDTO updateUser(UserRequestDTO request){
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        //Check username availability (amongst other users, not self)
        if(userRepository.existsByUsernameIgnoreCaseAndIdNot(request.username(), user.getId())){
            throw new IllegalArgumentException("Username is already in use");
        }
        //uppdate user
        user.setUsername(request.username());
        String rawPassword = request.password();
        user.setPassword(passwordEncoder.encode(rawPassword));

        User updatedUser = userRepository.save(user);

        //generate token
        String newToken = jwtService.generateToken(request.username());

        return new AuthResponseDTO(newToken);
    }

    /**
     * Update calorie and protein goals for a user
     * @param goals The updated goals
     */
    public MacroGoalsDTO updateUserMacroGoals(MacroGoalsDTO goals) {
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        user.setCalorieGoal(goals.calories());
        user.setProteinGoal(goals.protein());
        User savedUser = userRepository.save(user);

        return new MacroGoalsDTO(savedUser.getCalorieGoal(),  savedUser.getProteinGoal());
    }

    /**
     * Delete account for logged in user
     */
    public void deleteUser() {
        //get logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(()-> new EntityNotFoundException("User not found with username: " + username));

        userRepository.delete(user);
    }
}
