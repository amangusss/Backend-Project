package alatoo.edu.kg.api.service.impl;

import alatoo.edu.kg.api.exception.EmailTakenException;
import alatoo.edu.kg.api.exception.NotFoundException;
import alatoo.edu.kg.api.exception.UsernameTakenException;
import alatoo.edu.kg.api.mapper.UserMapper;
import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserLoginRequestDTO;
import alatoo.edu.kg.api.payload.user.UserLoginResponseDTO;
import alatoo.edu.kg.api.payload.user.UserRegisterRequestDTO;
import alatoo.edu.kg.api.service.AuthService;
import alatoo.edu.kg.api.service.RefreshTokenService;
import alatoo.edu.kg.api.utils.JwtUtils;
import alatoo.edu.kg.store.entity.RefreshToken;
import alatoo.edu.kg.store.entity.User;
import alatoo.edu.kg.store.enums.user.Roles;
import alatoo.edu.kg.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    public UserDTO register(UserRegisterRequestDTO dto) {

        if (repository.existsByUsername(dto.username())) {
            throw new UsernameTakenException();
        }

        if (repository.existsByEmail(dto.email())) {
            throw new EmailTakenException();
        }

        User user = userMapper.toUserFromRegisterRequest(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(Roles.USER));

        try {
            User savedUser = repository.save(user);
            return userMapper.toDTO(savedUser);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot register user: %s", e.getMessage()), e);
        }
    }

    @Override
    public UserLoginResponseDTO login(UserLoginRequestDTO dto) {
        User user = repository.findByUsername(dto.login())
                .orElseThrow(() -> new NotFoundException("The user was not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        dto.password()
                )
        );

        refreshTokenService.deleteByUserId(user.getId());

        String jwtToken = jwtUtils.generateToken(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new UserLoginResponseDTO(
                user.getUsername(),
                user.getRoles(),
                jwtToken,
                refreshToken.getToken()
        );
    }
}