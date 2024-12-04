package alatoo.edu.kg.api.controller.user;


import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserPublicDTO;
import alatoo.edu.kg.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public final class UserController implements UserControllerDocumentation {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserPublicDTO>> getAllUsers() {
        List<UserPublicDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserPublicDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        UserPublicDTO publicUser = new UserPublicDTO(user.getId(), user.getUsername());
        return ResponseEntity.ok(publicUser);
    }

    @Override
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        UserDTO updatedUser = userService.update(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
