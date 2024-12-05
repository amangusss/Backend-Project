package alatoo.edu.kg.api.mapper;

import alatoo.edu.kg.api.payload.reminder.ReminderRequestDTO;
import alatoo.edu.kg.api.payload.reminder.ReminderResponseDTO;
import alatoo.edu.kg.store.entity.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ReminderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "sent", constant = "false")
    Reminder toEntity(ReminderRequestDTO dto);

    @Mapping(target = "postId", source = "post.id")
    ReminderResponseDTO toDTO(Reminder entity);
}
