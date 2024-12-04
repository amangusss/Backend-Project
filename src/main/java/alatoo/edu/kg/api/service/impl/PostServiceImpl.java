package alatoo.edu.kg.api.service.impl;

import alatoo.edu.kg.api.exception.NotFoundException;
import alatoo.edu.kg.api.mapper.PostMapper;
import alatoo.edu.kg.api.payload.post.PostRequestDTO;
import alatoo.edu.kg.api.payload.post.PostResponseDTO;
import alatoo.edu.kg.api.service.PostService;
import alatoo.edu.kg.store.entity.Post;
import alatoo.edu.kg.store.entity.User;
import alatoo.edu.kg.store.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostMapper postMapper;
    PostRepository postRepository;

    @Override
    public PostResponseDTO createPost(PostRequestDTO dto) {
        Post post = postMapper.toEntity(dto);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setAuthor(currentUser);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return postMapper.toDTO(savedPost);
    }

    @Override
    @Transactional
    public PostResponseDTO updatePost(Long id, PostRequestDTO dto) {
        Post post = postRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not the author of this post");
        }

        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postRepository.save(post);
        return postMapper.toDTO(updatedPost);
    }

    @Override
    public PostResponseDTO getPost(Long id) {
        return postRepository.findByIdWithAuthor(id)
                .map(postMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Post with id " + id + " not found"));
    }

    @Override
    @Transactional
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAllWithAuthors();
        return posts.stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post existingPost = postRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!existingPost.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }

        postRepository.deleteById(id);
    }
}
