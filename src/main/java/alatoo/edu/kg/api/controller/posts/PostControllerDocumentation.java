package alatoo.edu.kg.api.controller.posts;

import alatoo.edu.kg.api.payload.post.PostRequestDTO;
import alatoo.edu.kg.api.payload.post.PostResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Endpoints for managing posts")
public sealed interface PostControllerDocumentation permits PostController {

    @Operation(summary = "Create a new post", description = "Creates a new post with title and content.")
    @PostMapping
    ResponseEntity<PostResponseDTO> createPost(
            @Parameter(description = "Details of the new post", required = true)
            @Valid @RequestBody PostRequestDTO dto);

    @Operation(summary = "Update a post", description = "Updates a post by its ID.")
    @PutMapping("/{id}")
    ResponseEntity<PostResponseDTO> updatePost(
            @Parameter(description = "ID of the post to be updated", required = true) @PathVariable Long id,
            @Parameter(description = "Updated details of the post", required = true) @Valid @RequestBody PostRequestDTO dto);

    @Operation(summary = "Get post by ID", description = "Fetches a specific post by its ID.")
    @GetMapping("/{id}")
    ResponseEntity<PostResponseDTO> getPost(
            @Parameter(description = "ID of the post to be retrieved", required = true)
            @PathVariable Long id);

    @Operation(summary = "Get all posts", description = "Fetches all available posts.")
    @GetMapping
    ResponseEntity<List<PostResponseDTO>> getAllPosts();


    @Operation(summary = "Delete a post", description = "Deletes a post by its ID.")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePost(
            @Parameter(description = "ID of the post to be deleted", required = true)
            @PathVariable Long id);
}
