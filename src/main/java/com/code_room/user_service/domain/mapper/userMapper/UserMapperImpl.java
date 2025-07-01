package com.code_room.user_service.domain.mapper.userMapper;


import com.code_room.user_service.domain.model.User;
import com.code_room.user_service.infrastructure.controller.dto.UserDto;
import com.code_room.user_service.infrastructure.repository.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDtoFromModel(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .identification(user.getIdentification())
                .role(user.getRole())
                .uriCvFile(user.getUriCvFile())
                .build();
    }

    @Override
    public User toModelFromDto(UserDto dto) {
        return User.builder()
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .identification(dto.getIdentification())
                .role(dto.getRole())
                .uriCvFile(dto.getUriCvFile())
                .build();
    }

    @Override
    public UserDto toDtoFromEntity(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .identification(entity.getIdentification())
                .role(entity.getRole())
                .uriCvFile(entity.getUriCvFile())
                .build();
    }

    @Override
    public UserEntity toEntityFromDto(UserDto dto) {
        return UserEntity.builder()
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .identification(dto.getIdentification())
                .role(dto.getRole())
                .uriCvFile(dto.getUriCvFile())
                .build();
    }

    @Override
    public User toModelFromEntity(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .identification(entity.getIdentification())
                .role(entity.getRole())
                .uriCvFile(entity.getUriCvFile())
                .build();
    }

    @Override
    public UserEntity toEntityFromModel(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .identification(user.getIdentification())
                .role(user.getRole())
                .uriCvFile(user.getUriCvFile())
                .build();
    }
}
