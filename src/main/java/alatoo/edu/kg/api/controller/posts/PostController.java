package alatoo.edu.kg.api.controller.posts;

import alatoo.edu.kg.api.payload.post.PostRequestDTO;
import alatoo.edu.kg.api.payload.post.PostResponseDTO;
import alatoo.edu.kg.api.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public final class PostController implements PostControllerDocumentation {

    private final PostService postService;

    @Override
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostRequestDTO dto) {
        PostResponseDTO createdPost = postService.createPost(dto);
        return ResponseEntity.status(201).body(createdPost);
    }

    @Override
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequestDTO dto) {
        PostResponseDTO updatedEntry = postService.updatePost(id, dto);
        return ResponseEntity.ok(updatedEntry);
    }

    @Override
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id) {
        PostResponseDTO entry = postService.getPost(id);
        return ResponseEntity.ok(entry);
    }

    @Override
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Override
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PostResponseDTO> uploadImages(@PathVariable Long postId, @Valid @RequestParam("images") List<MultipartFile> images) {
        PostResponseDTO updatedPost = postService.addImagesToPost(postId, images);
        return ResponseEntity.ok(updatedPost);
    }

    @Override
    public ResponseEntity<Void> deleteImage(@PathVariable Long postId, @PathVariable Long imageId) {
        postService.deleteImageFromPost(postId, imageId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> addPostToFavorites(@PathVariable Long id) {
        postService.addPostToFavorites(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removePostFromFavorites(@PathVariable Long id) {
        postService.removePostFromFavorites(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<PostResponseDTO>> getFavoritePosts() {
        List<PostResponseDTO> favorites = postService.getFavoritePosts();
        return ResponseEntity.ok(favorites);
    }

    @Override
    public ResponseEntity<List<PostResponseDTO>> getPostHistory(@PathVariable Long id) {
        List<PostResponseDTO> history = postService.getPostHistory(id);
        return ResponseEntity.ok(history);
    }

    @Override
    public ResponseEntity<PostResponseDTO> restorePostVersion(@PathVariable Long id, @RequestParam Integer version) {
        postService.restorePostVersion(id, version);
        PostResponseDTO restoredPost = postService.getPost(id);
        return ResponseEntity.ok(restoredPost);
    }
}
