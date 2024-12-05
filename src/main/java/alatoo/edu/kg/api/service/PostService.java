package alatoo.edu.kg.api.service;

import alatoo.edu.kg.api.payload.post.PostRequestDTO;
import alatoo.edu.kg.api.payload.post.PostResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO dto);
    PostResponseDTO updatePost(Long id, PostRequestDTO dto);
    PostResponseDTO getPost(Long id);
    List<PostResponseDTO> getAllPosts();
    void deletePost(Long id);

    PostResponseDTO addImagesToPost(Long postId, List<MultipartFile> images);
    void deleteImageFromPost(Long postId, Long imageId);

    void addPostToFavorites(Long postId);
    void removePostFromFavorites(Long postId);
    List<PostResponseDTO> getFavoritePosts();

    List<PostResponseDTO> getPostHistory(Long postId);
    void restorePostVersion(Long postId, Integer version);
}
