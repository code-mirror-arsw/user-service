package com.code_room.user_service.domain.ports;


import com.code_room.user_service.domain.Enum.Role;
import com.code_room.user_service.domain.model.User;
import com.code_room.user_service.infrastructure.controller.dto.LoginDto;
import com.code_room.user_service.infrastructure.controller.dto.UserDto;

public interface UserService {
    User findByEmail(String email);

    User findById(String id);

    Role getRoleByEmail(String email);

    UserDto checkPassword(LoginDto login);

    String createUser(UserDto userDto, String password);

    void verifyUser(String code);
}
