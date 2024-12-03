package alatoo.edu.kg.api.service.impl;

import alatoo.edu.kg.api.exception.EmailTakenException;
import alatoo.edu.kg.api.exception.NotFoundException;
import alatoo.edu.kg.api.exception.UsernameTakenException;
import alatoo.edu.kg.api.mapper.UserMapper;
import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserPublicDTO;
import alatoo.edu.kg.api.service.UserService;
import alatoo.edu.kg.store.entity.User;
import alatoo.edu.kg.store.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public UserDTO getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toDTO(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public List<UserPublicDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return users.stream()
                .map(userMapper::toPublicDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsername())) {
            if (repository.existsByUsername(dto.getUsername())) {
                throw new UsernameTakenException();
            }
            user.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (repository.existsByEmail(dto.getEmail())) {
                throw new EmailTakenException();
            }
            user.setEmail(dto.getEmail());
        }

        try {
            User updatedUser = repository.save(user);
            return userMapper.toDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Cannot update user: %s", e.getMessage()), e);
        }
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        repository.deleteById(id);
    }
}
