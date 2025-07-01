package com.code_room.user_service.domain.usecases;


import com.code_room.user_service.domain.Enum.Role;
import com.code_room.user_service.domain.mapper.userMapper.UserMapper;
import com.code_room.user_service.domain.model.User;
import com.code_room.user_service.domain.ports.UserService;
import com.code_room.user_service.infrastructure.controller.dto.LoginDto;
import com.code_room.user_service.infrastructure.controller.dto.UserDto;
import com.code_room.user_service.infrastructure.repository.UserRepository;
import com.code_room.user_service.infrastructure.repository.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User findByEmail(String email){
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User no found by email " + email));
        return userMapper.toModelFromEntity(entity);
    }

    @Override
    public User findById(String id){
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User no found by id " + id));
        return userMapper.toModelFromEntity(entity);
    }

    @Override
    public Role getRoleByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("no found "));
        return user.getRole();
    }

    @Override
    public UserDto checkPassword(LoginDto loginDto) {
        UserEntity user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.isEnabled()){
            throw new RuntimeException("usuario no habilitado");
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            System.out.println("error 2" );

            throw new RuntimeException("Invalid credentials");
        }

        User userModel = userMapper.toModelFromEntity(user);

        return userMapper.toDtoFromModel(userModel);
    }

    @Override
    public String createUser(UserDto userDto, String password) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists with this email.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        String verificationCode = generateVerificationCode();
        UserEntity userEntity = userMapper.toEntityFromDto(userDto);
        userEntity.setPassword(encodedPassword);
        userEntity.setVerificationCode(verificationCode);
        userEntity.setEnabled(false);

        userRepository.save(userEntity);

        return verificationCode;
    }

    private String generateVerificationCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    @Override
    @Transactional
    public void verifyUser(String code) {
        UserEntity user = userRepository.findByVerificationCode(code)
                        .orElseThrow(() -> new RuntimeException("code no found"));
        user.setEnabled(true);
        user.setVerificationCode(null);
        userRepository.save(user);
    }
}
