package com.code_room.user_service.domain.usecases;

import com.code_room.user_service.domain.Enum.Role;
import com.code_room.user_service.domain.Exception.CvUploadException;
import com.code_room.user_service.domain.mapper.userMapper.UserMapper;
import com.code_room.user_service.domain.ports.CvProcessingService;
import com.code_room.user_service.domain.ports.CvService;
import com.code_room.user_service.infrastructure.repository.UserRepository;
import com.code_room.user_service.infrastructure.repository.cvRepository.CvRepository;
import com.code_room.user_service.infrastructure.repository.cvRepository.dto.CvDto;
import com.code_room.user_service.infrastructure.repository.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class CvServiceImpl implements CvService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CvProcessingService cvProcessingService;

    @Override
    public void uploadUriCvFile(MultipartFile cvFile, String userId) throws IOException {
        validateFile(cvFile);

        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (entity.getRole() != Role.CLIENT) {
            throw new CvUploadException(CvUploadException.ErrorType.ONLY_CLIENT_CAN_UPLOAD);
        }

        CvDto cvDto = new CvDto(cvFile, entity, cvFile.getName());
        cvRepository.createCv(cvDto);

        cvProcessingService.processCvAndGenerateProfile(userId,cvFile);
    }

    @Override
    public byte[] getCvFile(String userId) throws IOException {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cvRepository.getCvFile(userMapper.toDtoFromEntity(entity));
    }

    @Override
    public void deleteCvFile(String userId) throws IOException {
        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cvRepository.deleteCvFile(userMapper.toDtoFromEntity(entity));
    }

    @Override
    public void updateCvFile(MultipartFile cvFile, String userId) throws IOException {
        validateFile(cvFile);

        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (entity.getRole() != Role.CLIENT) {
            throw new CvUploadException(CvUploadException.ErrorType.ONLY_CLIENT_CAN_UPLOAD);
        }

        CvDto cvDto = new CvDto(cvFile, entity, cvFile.getName());
        cvRepository.updateCv(cvDto);
    }

    private void validateFile(MultipartFile cvFile) {
        if (cvFile == null || cvFile.isEmpty()) {
            throw new IllegalArgumentException("The file does not exist or is null.");
        }

        String filename = cvFile.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }
    }

}
