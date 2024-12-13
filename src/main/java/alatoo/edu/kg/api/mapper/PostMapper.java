package alatoo.edu.kg.api.mapper;

import alatoo.edu.kg.api.payload.post.PostRequestDTO;
import alatoo.edu.kg.api.payload.post.PostResponseDTO;
import alatoo.edu.kg.store.entity.Post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {UserMapper.class, ImageMapper.class})
public interface PostMapper {

    @Mapping(target = "currentVersion", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "images", ignore = true)
    Post toEntity(PostRequestDTO dto);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "images", source = "images")
    PostResponseDTO toDTO(Post entity);
}
