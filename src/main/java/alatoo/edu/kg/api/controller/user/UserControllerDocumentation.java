package alatoo.edu.kg.api.controller.user;

import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserPublicDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public sealed interface UserControllerDocumentation permits UserController {

    @Operation(summary = "Get all users", description = "Fetches all users with public information.")
    @GetMapping
    ResponseEntity<List<UserPublicDTO>> getAllUsers();

    @Operation(summary = "Get user by ID", description = "Fetches public information of a specific user by ID.")
    @GetMapping("/{id}")
    ResponseEntity<UserPublicDTO> getUserById(
            @Parameter(description = "ID of the user to be retrieved", required = true)
            @PathVariable Long id);

    @Operation(summary = "Update user details", description = "Updates a specific user's details.")
    @PutMapping("/{id}")
    ResponseEntity<UserDTO> update(
            @Parameter(description = "ID of the user to be updated", required = true) @PathVariable Long id,
            @Parameter(description = "Updated user details", required = true) @Valid @RequestBody UserDTO dto);

    @Operation(summary = "Delete a user", description = "Deletes a specific user by ID.")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable Long id);
}
