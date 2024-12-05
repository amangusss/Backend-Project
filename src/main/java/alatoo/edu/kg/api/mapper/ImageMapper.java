package alatoo.edu.kg.api.mapper;

import alatoo.edu.kg.api.payload.image.ImageDTO;
import alatoo.edu.kg.store.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ImageMapper {

    ImageDTO toDTO(Image image);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    Image toEntity(ImageDTO dto);
}
