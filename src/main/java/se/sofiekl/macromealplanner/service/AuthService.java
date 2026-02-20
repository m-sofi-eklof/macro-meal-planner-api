package se.sofiekl.macromealplanner.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sofiekl.macromealplanner.dto.userAndLogin.AuthResponseDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.LoginRequestDTO;
import se.sofiekl.macromealplanner.dto.userAndLogin.RegisterRequestDTO;
import se.sofiekl.macromealplanner.model.User;
import se.sofiekl.macromealplanner.repository.UserRepository;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    /**
     * Register a new User
     * @param request The request data for the User
     * @return The token wrapped in a DTO
     */
    public AuthResponseDTO register(RegisterRequestDTO request) {
        User user = userService.createUser(request.username(), request.password());
        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponseDTO(token);
    }


    /**
     * Login a User
     * @param request The request data of the User
     * @return Token wrapped in a DTO
     */
    public AuthResponseDTO login(LoginRequestDTO request){
        //trigger CustomUserDetaials + PasswordEncoder check
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String token = jwtService.generateToken(request.username());
        return new AuthResponseDTO(token);
    }

    public AuthResponseDTO generateTokens(UserDetails userDetails) {
        String accessToken = jwtService.generateToken(userDetails.getUsername());
        return new AuthResponseDTO(accessToken);
    }

}
