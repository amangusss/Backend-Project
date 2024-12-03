package alatoo.edu.kg.api.service;

import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserPublicDTO;
import alatoo.edu.kg.store.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO getUserById(Long id);
    List<UserPublicDTO> getAllUsers();
    Optional<User> findByUsername(String username);
    UserDTO update(Long id, UserDTO dto);
    void delete(Long id);
}
