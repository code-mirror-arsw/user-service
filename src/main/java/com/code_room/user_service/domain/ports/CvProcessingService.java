package com.code_room.user_service.domain.ports;

import com.code_room.user_service.infrastructure.controller.dto.PublicProfileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CvProcessingService {
    PublicProfileDto processCvAndGenerateProfile(String userId, MultipartFile cvFile) throws IOException;
}