package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get a single User (by ID)
     * @param userId The User's ID
     * @return The User's response data
     */
    public UserResponseDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserMapper::toDTO)
                .orElseThrow(()->new EntityNotFoundException("User not found with id: "+userId));
    }

    /**
     * Get a single User (by username)
     * @param username the User's username
     * @return The user response data
     */
    UserResponseDTO getUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .map(UserMapper::toDTO)
                .orElseThrow(()->new EntityNotFoundException("User not found with username: "+username));
    }

    /**
     * Add a new User
     * @param request The request data for the User
     * @return The response data for the User
     */
    public UserResponseDTO addUser(UserRequestDTO request){
        //check username availability
        if (userRepository.existsByUsernameIgnoreCase(request.username())){
            throw new IllegalArgumentException("Username is already in use");
        }
        //create user
        User user = UserMapper.fromDTO(request);

        //hash password
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));

        //save
        User savedUser = userRepository.save(user);
        UserResponseDTO userResponseDTO = UserMapper.toDTO(savedUser);
        return userResponseDTO;
    }

    /**
     * Update a User (password and username only)
     * @param userId The ID of the User
     * @param request The requestData of the user
     * @return
     */
    public UserResponseDTO updateUser(Long userId, UserRequestDTO request){
        //Find user
        User foundUser = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: "+userId));
        //Check username availability (amongst other users, not self)
        if(userRepository.existsByUsernameIgnoreCaseAndIdNot(request.username(), foundUser.getId())){
            throw new IllegalArgumentException("Username is already in use");
        }
        //uppdate user
        foundUser.setUsername(request.username());
        String rawPassword = request.password();
        foundUser.setPassword(passwordEncoder.encode(rawPassword));

        User updatedUser = userRepository.save(foundUser);
        //return
        return UserMapper.toDTO(updatedUser);

    }

    /**
     * Update protein goals for a user
     * @param userId The user ID
     * @param proteinGoal The new protein goal
     */
    public void updateUserProteinGoal(Long userId, Double proteinGoal) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: "+userId));

        user.setProteinGoal(proteinGoal);

        userRepository.save(user);
    }

    /**
     * Update calorie goals for a user
     * @param userId The ID of the User
     * @param caloriesGoal The updated calorie goal
     */
    public void updateUserCaloriesGoal(Long userId, Integer caloriesGoal) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: "+userId));
        user.setCalorieGoal(caloriesGoal);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        Optional<User> optionalUserser = userRepository.findById(userId);
        if(optionalUserser.isEmpty()){
            throw new IllegalArgumentException("User not found with id: "+userId);
        }

        User user = optionalUserser.get();
        userRepository.delete(user);
    }
}
