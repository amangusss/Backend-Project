package alatoo.edu.kg.api.service.impl;

import alatoo.edu.kg.api.exception.EmailTakenException;
import alatoo.edu.kg.api.exception.FileOperationException;
import alatoo.edu.kg.api.exception.NotFoundException;
import alatoo.edu.kg.api.exception.UsernameTakenException;
import alatoo.edu.kg.api.mapper.UserMapper;
import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserPublicDTO;
import alatoo.edu.kg.api.service.UserService;
import alatoo.edu.kg.store.entity.User;
import alatoo.edu.kg.store.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    UserRepository repository;
    UserMapper userMapper;
    UserDetailsService userDetailsService;

    @Override
    public UserDTO getUserById(Long id) {
        User user = repository.findByIdWithFavoritePosts(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<UserPublicDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(userMapper::toPublicDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        boolean usernameChanged = false;
        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (repository.existsByUsername(dto.getUsername())) {
                throw new UsernameTakenException();
            }
            user.setUsername(dto.getUsername());
            usernameChanged = true;
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (repository.existsByEmail(dto.getEmail())) {
                throw new EmailTakenException();
            }
            user.setEmail(dto.getEmail());
        }

        try {
            User updatedUser = repository.save(user);
            UserDTO userDTO = userMapper.toDTO(updatedUser);

            if (usernameChanged) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated()) {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof UserDetails) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(updatedUser.getUsername());
                        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                authentication.getCredentials(),
                                userDetails.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(newAuth);
                        logger.info("Authentication updated in SecurityContextHolder after username change.");
                    }
                }
            }

            return userDTO;
        } catch (Exception e) {
            throw new FileOperationException("Cannot update user: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
}
