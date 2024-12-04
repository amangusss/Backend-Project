package alatoo.edu.kg.api.service;

import alatoo.edu.kg.api.payload.post.PostRequestDTO;
import alatoo.edu.kg.api.payload.post.PostResponseDTO;

import java.util.List;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO dto);
    PostResponseDTO updatePost(Long id, PostRequestDTO dto);
    PostResponseDTO getPost(Long id);
    List<PostResponseDTO> getAllPosts();
    void deletePost(Long id);
}
