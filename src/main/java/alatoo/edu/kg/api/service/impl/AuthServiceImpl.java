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
import alatoo.edu.kg.api.service.EmailService;
import alatoo.edu.kg.api.service.RefreshTokenService;
import alatoo.edu.kg.api.utils.JwtUtils;
import alatoo.edu.kg.store.entity.PasswordResetToken;
import alatoo.edu.kg.store.entity.User;
import alatoo.edu.kg.store.entity.UserDetailsImpl;
import alatoo.edu.kg.store.entity.VerificationToken;
import alatoo.edu.kg.store.enums.user.Roles;
import alatoo.edu.kg.store.repository.PasswordResetTokenRepository;
import alatoo.edu.kg.store.repository.UserRepository;
import alatoo.edu.kg.store.repository.VerificationTokenRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    UserRepository userRepository;
    VerificationTokenRepository verificationTokenRepository;
    PasswordResetTokenRepository passwordResetTokenRepository;
    EmailService emailService;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    RefreshTokenService refreshTokenService;
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;

    @Override
    public UserDTO register(UserRegisterRequestDTO dto) {
        logger.info("Attempting to register user with username='{}' and email='{}'", dto.getUsername(), dto.getEmail());

        if (dto.getUsername() != null && userRepository.existsByUsername(dto.getUsername())) {
            logger.warn("Username '{}' is already taken.", dto.getUsername());
            throw new UsernameTakenException();
        }
        if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
            logger.warn("Email '{}' is already taken.", dto.getEmail());
            throw new EmailTakenException();
        }

        User user = userMapper.toUserFromRegisterRequest(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(false);
        user.setRoles(Collections.singleton(Roles.USER));
        user.setProvider("LOCAL");

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID={}", savedUser.getId());

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(savedUser)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        verificationTokenRepository.save(verificationToken);
        logger.info("Verification token generated and saved for userId={}", savedUser.getId());

        String confirmationUrl = "http://localhost:8082/api/auth/confirm?token=" + token;
        String message = "Please confirm your registration by clicking the link: " + confirmationUrl;
        emailService.sendEmail(savedUser.getEmail(), "Email Confirmation", message);
        logger.info("Confirmation email sent to '{}'", savedUser.getEmail());

        return userMapper.toDTO(savedUser);
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO dto) {
        logger.info("User attempting to log in with login='{}'", dto.login());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.login(), dto.password())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.isEnabled()) {
            logger.warn("User account '{}' is not enabled.", userDetails.getUsername());
            throw new NotFoundException("User account is not enabled");
        }

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userDetails.getId()));

        String accessToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        logger.info("User '{}' logged in successfully. Tokens generated.", user.getUsername());

        return UserLoginResponseDTO.builder()
                .username(user.getUsername())
                .roles(user.getRoles())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        logger.info("Attempting to logout with refresh token='{}'", refreshToken);
        refreshTokenService.delete(refreshToken);
        SecurityContextHolder.clearContext();
        logger.info("Refresh token '{}' has been successfully deleted and SecurityContext cleared.", refreshToken);
    }

    @Override
    public void confirmRegistration(String token) {
        logger.info("Attempting to confirm registration with token='{}'", token);

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    logger.error("Invalid verification token='{}'", token);
                    return new NotFoundException("Invalid verification token");
                });

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.error("Verification token expired token='{}'", token);
            throw new NotFoundException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        logger.info("User '{}' has been enabled successfully.", user.getUsername());

        verificationTokenRepository.delete(verificationToken);
        logger.info("Verification token '{}' has been deleted.", token);
    }

    @Override
    public void initiatePasswordReset(String email) {
        logger.info("Initiating password reset for email='{}'", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("No user found with email='{}'", email);
                    return new NotFoundException("User not found");
                });

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(2))
                .build();

        passwordResetTokenRepository.save(resetToken);
        logger.info("Password reset token generated and saved for userId={}", user.getId());

        String message = "To reset your password, copy the token: " + token;
        emailService.sendEmail(user.getEmail(), "Password Reset Request", message);
        logger.info("Password reset email sent to '{}'", user.getEmail());
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        logger.info("Attempting to reset password with token='{}'", token);

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    logger.error("Invalid password reset token='{}'", token);
                    return new NotFoundException("Invalid password reset token");
                });

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.error("Password reset token expired token='{}'", token);
            throw new NotFoundException("Password reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("Password has been reset successfully for user '{}'", user.getUsername());

        passwordResetTokenRepository.delete(resetToken);
        logger.info("Password reset token '{}' has been deleted.", token);
    }
}
