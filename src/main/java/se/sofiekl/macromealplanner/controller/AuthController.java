package se.sofiekl.macromealplanner.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sofiekl.macromealplanner.dto.userAndLogin.AuthResponseDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.LoginRequestDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.RegisterRequestDTO;
import se.sofiekl.macromealplanner.service.AuthService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new User
     * @param request The register request data
     * @return The token data object + 201CREATED
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login a user
     * @param request The login request data
     * @return The token data object + 200OK
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
