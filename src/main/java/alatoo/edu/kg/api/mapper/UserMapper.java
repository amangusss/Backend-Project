package alatoo.edu.kg.api.mapper;

import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserLoginResponseDTO;
import alatoo.edu.kg.api.payload.user.UserPublicDTO;
import alatoo.edu.kg.api.payload.user.UserRegisterRequestDTO;
import alatoo.edu.kg.store.entity.User;

import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {
    UserDTO toDTO(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toUserFromRegisterRequest(UserRegisterRequestDTO dto);

    @Mapping(target = "accessToken", ignore = true)
    UserLoginResponseDTO toLoginResponseDTO(User user);

    UserPublicDTO toPublicDto(User user);

    default UserLoginResponseDTO toLoginResponseDTOWithToken(User user, String accessToken) {
        UserLoginResponseDTO dto = toLoginResponseDTO(user);
        dto.setAccessToken(accessToken);
        return dto;
    }
}