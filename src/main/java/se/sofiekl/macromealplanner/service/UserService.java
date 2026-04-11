package se.sofiekl.macromealplanner.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.BodyStatsDTO;
import se.sofiekl.macromealplanner.dto.MacroCalculationRequestDTO;
import se.sofiekl.macromealplanner.dto.MacroCalculationResponseDTO;
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
     * Get calorie and protein goals for the logged-in user
     * @return MacroGoalsDTO with current goals
     */
    public MacroGoalsDTO getUserMacroGoals() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        return new MacroGoalsDTO(user.getCalorieGoal(), user.getProteinGoal());
    }

    /**
     * Get body stats for the logged-in user
     */
    public BodyStatsDTO getBodyStats() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        return new BodyStatsDTO(user.getGender(), user.getAge(), user.getWeightKg(), user.getHeightCm(), user.getActivityLevel());
    }

    /**
     * Save body stats for the logged-in user
     */
    @Transactional
    public BodyStatsDTO saveBodyStats(BodyStatsDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        user.setGender(dto.gender());
        user.setAge(dto.age());
        user.setWeightKg(dto.weightKg());
        user.setHeightCm(dto.heightCm());
        user.setActivityLevel(dto.activityLevel());
        User saved = userRepository.save(user);

        return new BodyStatsDTO(saved.getGender(), saved.getAge(), saved.getWeightKg(), saved.getHeightCm(), saved.getActivityLevel());
    }

    /**
     * Calculate recommended macros based on body stats and goal — does not persist anything
     */
    public MacroCalculationResponseDTO calculateMacros(MacroCalculationRequestDTO req) {
        double bmrRaw = (10 * req.weightKg()) + (6.25 * req.heightCm()) - (5 * req.age());
        double bmr = switch (req.gender()) {
            case MALE -> bmrRaw + 5;
            case FEMALE -> bmrRaw - 161;
        };

        double tdee = bmr * req.activityLevel().getMultiplier();

        int adjustedCalories = switch (req.goal()) {
            case BULK -> (int) Math.round(tdee + 400);
            case RECOMP -> (int) Math.round(tdee);
            case CUT -> (int) Math.round(tdee - 500);
        };

        double proteinMultiplier = switch (req.goal()) {
            case BULK -> 1.8;
            case RECOMP -> 2.0;
            case CUT -> 2.2;
        };
        double protein = Math.round(proteinMultiplier * req.weightKg() * 10.0) / 10.0;

        return new MacroCalculationResponseDTO(adjustedCalories, protein, (int) tdee, (int) bmr);
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
