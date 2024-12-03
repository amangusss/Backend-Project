package alatoo.edu.kg.api.service;

import alatoo.edu.kg.api.payload.user.UserDTO;
import alatoo.edu.kg.api.payload.user.UserLoginRequestDTO;
import alatoo.edu.kg.api.payload.user.UserLoginResponseDTO;
import alatoo.edu.kg.api.payload.user.UserRegisterRequestDTO;

public interface AuthService {
    UserDTO register(UserRegisterRequestDTO dto);
    UserLoginResponseDTO login(UserLoginRequestDTO dto);
}
