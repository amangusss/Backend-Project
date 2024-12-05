package alatoo.edu.kg.api.payload.post;

import alatoo.edu.kg.api.payload.image.ImageDTO;
import alatoo.edu.kg.api.payload.user.UserPublicDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponseDTO {
    Long id;
    String title;
    String description;
    Integer version;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UserPublicDTO author;
    List<ImageDTO> images;
    boolean isFavorite;
}
