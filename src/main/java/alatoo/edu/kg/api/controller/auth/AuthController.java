package alatoo.edu.kg.api.controller.auth;


import alatoo.edu.kg.api.exception.TokenRefreshException;
import alatoo.edu.kg.api.payload.token.TokenRefreshRequest;
import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserLoginRequestDTO;
import alatoo.edu.kg.api.payload.user.UserLoginResponseDTO;
import alatoo.edu.kg.api.payload.user.UserRegisterRequestDTO;
import alatoo.edu.kg.api.service.AuthService;
import alatoo.edu.kg.api.service.RefreshTokenService;
import alatoo.edu.kg.api.utils.JwtUtils;
import alatoo.edu.kg.store.entity.RefreshToken;
import alatoo.edu.kg.store.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public final class AuthController implements AuthControllerDocumentation {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegisterRequestDTO dto) {
        UserDTO registeredUser = authService.register(dto);
        return ResponseEntity.status(201).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO dto) {
        UserLoginResponseDTO loginResponse = authService.login(dto);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            refreshTokenService.deleteByUserId(user.getId());
            return ResponseEntity.ok("You have successfully logged out.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The user is not logged in.");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    refreshTokenService.deleteByUserId(user.getId());

                    String newAccessToken = jwtUtils.generateToken(user);

                    String newRefreshToken = refreshTokenService.createRefreshToken(user).getToken();

                    UserLoginResponseDTO response = new UserLoginResponseDTO(
                            user.getUsername(),
                            user.getRoles(),
                            newAccessToken,
                            newRefreshToken
                    );

                    return ResponseEntity.ok(response);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token was not found."));
    }
}
