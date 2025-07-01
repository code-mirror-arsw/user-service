package com.code_room.user_service.infrastructure.repository.cvRepository.dto;

import com.code_room.user_service.infrastructure.repository.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@AllArgsConstructor
public class CvDto {
    MultipartFile file;
    UserEntity User;
    String name;
}
