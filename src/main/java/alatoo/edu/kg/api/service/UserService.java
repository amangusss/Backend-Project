package alatoo.edu.kg.api.service;

import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserPublicDTO;
import alatoo.edu.kg.store.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<UserPublicDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO update(Long id, UserDTO dto);
    void delete(Long id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
