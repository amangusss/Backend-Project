package alatoo.edu.kg.api.payload.post;

import alatoo.edu.kg.store.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequestDTO {

    @NotBlank
    String title;

    @NotBlank
    String description;
}
