package se.sofiekl.macromealplanner.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.macromealplanner.dto.BodyStatsDTO;
import se.sofiekl.macromealplanner.dto.MacroCalculationRequestDTO;
import se.sofiekl.macromealplanner.dto.MacroCalculationResponseDTO;
import se.sofiekl.macromealplanner.dto.MacroGoalsDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.AuthResponseDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.UserRequestDTO;
import se.sofiekl.macromealplanner.service.UserService;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Update user pass and or username
     * @param userRequestDTO The request with updated fields
     * @return 200OK + the user response data
     */
    @PatchMapping
    public ResponseEntity<AuthResponseDTO> updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return  ResponseEntity.ok(userService.updateUser(userRequestDTO));
    }

    /**
     * Update user goals for calories and protein per day
     * @param goals The request data for the updated goals
     * @return A MacroGoalsDTO of with the updated macro goals for user
     */
    @PutMapping("/goals")
    public ResponseEntity<MacroGoalsDTO> updateMacroGoals(@Valid @RequestBody MacroGoalsDTO goals) {
        return ResponseEntity.ok(userService.updateUserMacroGoals(goals));
    }

    /**
     * Get macro goals for the logged-in user
     * @return MacroGoalsDTO with current goals
     */
    @GetMapping("/goals")
    public ResponseEntity<MacroGoalsDTO> getMacroGoals() {
        return ResponseEntity.ok(userService.getUserMacroGoals());
    }

    /**
     * Get body stats for the logged-in user
     */
    @GetMapping("/stats")
    public ResponseEntity<BodyStatsDTO> getBodyStats() {
        return ResponseEntity.ok(userService.getBodyStats());
    }

    /**
     * Save body stats for the logged-in user
     */
    @PutMapping("/stats")
    public ResponseEntity<BodyStatsDTO> saveBodyStats(@RequestBody BodyStatsDTO dto) {
        return ResponseEntity.ok(userService.saveBodyStats(dto));
    }

    /**
     * Calculate recommended macros based on provided stats and goal
     */
    @PostMapping("/calculate-macros")
    public ResponseEntity<MacroCalculationResponseDTO> calculateMacros(@RequestBody MacroCalculationRequestDTO dto) {
        return ResponseEntity.ok(userService.calculateMacros(dto));
    }

    /**
     * Delete logged-in user
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }

}
