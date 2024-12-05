package alatoo.edu.kg.api.controller.posts;

import alatoo.edu.kg.api.payload.post.PostRequestDTO;
import alatoo.edu.kg.api.payload.post.PostResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Endpoints for managing posts")
public sealed interface PostControllerDocumentation permits PostController {

    @Operation(summary = "Create a new post", description = "Creates a new post with title and content.")
    @PostMapping
    ResponseEntity<PostResponseDTO> createPost(
            @Parameter(description = "Details of the new post", required = true) @Valid @RequestBody PostRequestDTO dto);

    @Operation(summary = "Update a post", description = "Updates a post by its ID.")
    @PutMapping("/{id}")
    ResponseEntity<PostResponseDTO> updatePost(
            @Parameter(description = "ID of the post to be updated", required = true) @PathVariable Long id,
            @Parameter(description = "Updated details of the post", required = true) @Valid @RequestBody PostRequestDTO dto);

    @Operation(summary = "Get post by ID", description = "Fetches a specific post by its ID.")
    @GetMapping("/{id}")
    ResponseEntity<PostResponseDTO> getPost(
            @Parameter(description = "ID of the post to be retrieved", required = true) @PathVariable Long id);

    @Operation(summary = "Get all posts", description = "Fetches all available posts.")
    @GetMapping
    ResponseEntity<List<PostResponseDTO>> getAllPosts();


    @Operation(summary = "Delete a post", description = "Deletes a post by its ID.")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePost(
            @Parameter(description = "ID of the post to be deleted", required = true) @PathVariable Long id);

    @Operation(summary = "Upload images to post", description = "Uploads images by ID of the post")
    @PostMapping("{postId}/images")
    ResponseEntity<PostResponseDTO> uploadImages(
            @Parameter(description = "Id of the post", required = true) @PathVariable Long postId,
            @Parameter(description = "List of images", required = true) @Valid @RequestBody List<MultipartFile> images);

    @Operation(summary = "Delete an image", description = "Deletes an image of the post by its ID")
    @DeleteMapping("/{postId}/images/{imageId}")
    ResponseEntity<Void> deleteImage(
            @Parameter(description = "Id of the post", required = true) @PathVariable Long postId,
            @Parameter(description = "Id of the image", required = true) @PathVariable Long imageId);

    @Operation(summary = "Add post to favorites", description = "Marks a post as favorite.")
    @PostMapping("/{id}/favorite")
    ResponseEntity<Void> addPostToFavorites(
            @Parameter(description = "ID of the post to be marked as favorite", required = true) @PathVariable Long id);

    @Operation(summary = "Remove post from favorites", description = "Removes a post from favorites.")
    @DeleteMapping("/{id}/favorite")
    ResponseEntity<Void> removePostFromFavorites(
            @Parameter(description = "ID of the post to be removed from favorites", required = true) @PathVariable Long id);

    @Operation(summary = "Get favorite posts", description = "Retrieves all favorite posts of the current user.")
    @GetMapping("/favorites")
    ResponseEntity<List<PostResponseDTO>> getFavoritePosts();

    @Operation(summary = "Get post history", description = "Fetches the history of a post by its ID.")
    @GetMapping("/{id}/history")
    ResponseEntity<List<PostResponseDTO>> getPostHistory(
            @Parameter(description = "ID of the post", required = true) @PathVariable Long id);

    @Operation(summary = "Restore post version", description = "Restores a post to a specific version.")
    @PostMapping("/{id}/restore")
    ResponseEntity<PostResponseDTO> restorePostVersion(
            @Parameter(description = "ID of the post", required = true) @PathVariable Long id,
            @Parameter(description = "Version number to restore", required = true) @RequestParam Integer version);
}
