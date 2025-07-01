package com.code_room.user_service.domain.ports;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface CvService {
    void uploadUriCvFile(MultipartFile cvFile, String userId) throws IOException;

    byte[] getCvFile(String userId) throws IOException;

    void deleteCvFile(String userId) throws IOException;

    void updateCvFile(MultipartFile cvFile, String userId) throws IOException;
}
