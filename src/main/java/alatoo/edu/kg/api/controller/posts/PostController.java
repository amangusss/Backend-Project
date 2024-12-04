package alatoo.edu.kg.api.controller.posts;

import alatoo.edu.kg.api.payload.post.PostRequestDTO;
import alatoo.edu.kg.api.payload.post.PostResponseDTO;
import alatoo.edu.kg.api.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
