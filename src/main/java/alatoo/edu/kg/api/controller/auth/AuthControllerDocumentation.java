package alatoo.edu.kg.api.controller.auth;

import alatoo.edu.kg.api.payload.token.TokenRefreshRequest;
import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserLoginRequestDTO;
import alatoo.edu.kg.api.payload.user.UserLoginResponseDTO;
import alatoo.edu.kg.api.payload.user.UserRegisterRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public sealed interface AuthControllerDocumentation permits AuthController{

    @Operation(summary = "Register a new user", description = "Registers a new user with the given details.")
    @PostMapping("/register")
    ResponseEntity<UserDTO> register(
            @Parameter(description = "User registration details", required = true)
            @RequestBody @Valid UserRegisterRequestDTO dto);

    @Operation(summary = "Login a user", description = "Allows a registered user to login with username and password.")
    @PostMapping("/login")
    ResponseEntity<UserLoginResponseDTO> login(
            @Parameter(description = "User login details", required = true)
            @RequestBody @Valid UserLoginRequestDTO dto);

    @Operation(summary = "Refresh JWT Token", description = "Refreshes the JWT access token using a valid refresh token.")
    @PostMapping("/refresh-token")
    ResponseEntity<?> refreshToken(
            @Parameter(description = "Refresh token request", required = true)
            @RequestBody @Valid TokenRefreshRequest request);

    @Operation(summary = "Logout user", description = "Logs out the current user and invalidates the refresh token.")
    @PostMapping("/logout")
    ResponseEntity<?> logout();
}
