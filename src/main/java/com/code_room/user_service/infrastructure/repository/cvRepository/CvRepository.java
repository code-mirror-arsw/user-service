package com.code_room.user_service.infrastructure.repository.cvRepository;

import com.code_room.user_service.infrastructure.controller.dto.UserDto;
import com.code_room.user_service.infrastructure.repository.cvRepository.dto.CvDto;

import java.io.IOException;

public interface CvRepository {
    UserDto createCv(CvDto dto) throws IOException;

    byte[] getCvFile(UserDto user) throws IOException;

    void deleteCvFile(UserDto user) throws IOException;

    void updateCv(CvDto dto) throws IOException;
}
