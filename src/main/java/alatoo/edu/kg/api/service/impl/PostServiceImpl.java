package alatoo.edu.kg.api.service.impl;

import alatoo.edu.kg.api.exception.FileOperationException;
import alatoo.edu.kg.api.exception.NotFoundException;
import alatoo.edu.kg.api.exception.UnauthorizedActionException;
import alatoo.edu.kg.api.mapper.PostMapper;
import alatoo.edu.kg.api.mapper.UserMapper;
import alatoo.edu.kg.api.payload.post.PostRequestDTO;
import alatoo.edu.kg.api.payload.post.PostResponseDTO;
import alatoo.edu.kg.api.service.PostService;
import alatoo.edu.kg.store.entity.Image;
import alatoo.edu.kg.store.entity.Post;
import alatoo.edu.kg.store.entity.PostHistory;
import alatoo.edu.kg.store.entity.User;
import alatoo.edu.kg.store.repository.ImageRepository;
import alatoo.edu.kg.store.repository.PostHistoryRepository;
import alatoo.edu.kg.store.repository.PostRepository;

import alatoo.edu.kg.store.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostMapper postMapper;
    PostRepository postRepository;
    ImageRepository imageRepository;
    UserRepository userRepository;
    PostHistoryRepository postHistoryRepository;
    UserMapper userMapper;

    @Override
    public PostResponseDTO createPost(PostRequestDTO dto) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postMapper.toEntity(dto);
        post.setAuthor(currentUser);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return postMapper.toDTO(savedPost);
    }

    @Override
    public PostResponseDTO updatePost(Long id, PostRequestDTO dto) {
        Post post = postRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You are not the author of this post");
        }

        savePostHistory(post, currentUser);

        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setUpdatedAt(LocalDateTime.now());
        post.setCurrentVersion(post.getCurrentVersion() + 1);

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
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAllWithAuthors();
        return posts.stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePost(Long id) {
        Post existingPost = postRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!existingPost.getAuthor().getUsername().equals(username)) {
            throw new UnauthorizedActionException("You are not authorized to delete this post");
        }

        postRepository.deleteById(id);
    }

    @Override
    public PostResponseDTO addImagesToPost(Long postId, List<MultipartFile> images) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You are not the author of this post");
        }

        for (MultipartFile imageFile : images) {
            String imageUrl = saveImageToFileSystem(imageFile);

            Image image = new Image();
            image.setUrl(imageUrl);
            image.setPost(post);
            post.getImages().add(image);
        }

        Post updatedPost = postRepository.save(post);
        return postMapper.toDTO(updatedPost);
    }

    private String saveImageToFileSystem(MultipartFile imageFile) {
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get("uploads/" + fileName);

        try {
            Files.createDirectories(imagePath.getParent());
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileOperationException("Failed to save image", e);
        }

        return "/uploads/" + fileName;
    }

    @Override
    public void deleteImageFromPost(Long postId, Long imageId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You are not the author of this post");
        }

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found with id: " + imageId));

        if (!image.getPost().getId().equals(postId)) {
            throw new NotFoundException("Image does not belong to the given post");
        }

        deleteImageFromFileSystem(image.getUrl());

        post.getImages().remove(image);
        imageRepository.delete(image);
    }

    private void deleteImageFromFileSystem(String imageUrl) {
        Path imagePath = Paths.get(imageUrl.replaceFirst("/", ""));
        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new FileOperationException("Failed to delete image", e);
        }
    }

    @Override
    public void addPostToFavorites(Long postId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        currentUser.getFavoritePosts().add(post);
        userRepository.save(currentUser);
    }

    @Override
    public void removePostFromFavorites(Long postId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        currentUser.getFavoritePosts().remove(post);
        userRepository.save(currentUser);
    }

    @Override
    public List<PostResponseDTO> getFavoritePosts() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Post> favoritePosts = currentUser.getFavoritePosts();

        return favoritePosts.stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDTO> getPostHistory(Long postId) {
        List<PostHistory> historyList = postHistoryRepository.findByPostIdOrderByVersionDesc(postId);
        return historyList.stream()
                .map(this::convertHistoryToPostResponseDTO)
                .collect(Collectors.toList());
    }

    private PostResponseDTO convertHistoryToPostResponseDTO(PostHistory history) {
        return PostResponseDTO.builder()
                .id(history.getPost().getId())
                .title(history.getTitle())
                .description(history.getDescription())
                .createdAt(history.getModifiedAt())
                .updatedAt(history.getModifiedAt())
                .author(userMapper.toPublicDto(history.getModifiedBy()))
                .version(history.getVersion())
                .build();
    }

    private void savePostHistory(Post post, User modifiedBy) {
        PostHistory history = new PostHistory();
        history.setPost(post);
        history.setVersion(post.getCurrentVersion());
        history.setTitle(post.getTitle());
        history.setDescription(post.getDescription());
        history.setModifiedAt(LocalDateTime.now());
        history.setModifiedBy(modifiedBy);

        postHistoryRepository.save(history);
    }

    @Override
    public void restorePostVersion(Long postId, Integer version) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You are not the author of this post");
        }

        PostHistory history = postHistoryRepository.findByPostIdAndVersion(postId, version)
                .orElseThrow(() -> new NotFoundException("Version not found"));

        savePostHistory(post, currentUser);

        post.setTitle(history.getTitle());
        post.setDescription(history.getDescription());
        post.setUpdatedAt(LocalDateTime.now());
        post.setCurrentVersion(history.getVersion());

        postRepository.save(post);
    }
}

