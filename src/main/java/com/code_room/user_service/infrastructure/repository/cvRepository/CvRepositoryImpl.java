package com.code_room.user_service.infrastructure.repository.cvRepository;

import com.code_room.user_service.domain.mapper.userMapper.UserMapper;
import com.code_room.user_service.infrastructure.controller.dto.UserDto;
import com.code_room.user_service.infrastructure.repository.UserRepository;
import com.code_room.user_service.infrastructure.repository.cvRepository.dto.CvDto;
import com.code_room.user_service.infrastructure.repository.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

@Repository
public class CvRepositoryImpl implements CvRepository{
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Value("${file.path-save}")
    private String path;


    @Override
    public UserDto createCv(CvDto dto) throws IOException {
        Path filepath = Paths.get(path, "cv", dto.getUser().getId(), dto.getName());
        Path dirPath = filepath.getParent();

        // Crear directorio
        Files.createDirectories(dirPath);

        // Establecer permisos 755 al directorio
        try {
            Set<PosixFilePermission> dirPermissions = PosixFilePermissions.fromString("rwxr-xr-x");
            Files.setPosixFilePermissions(dirPath, dirPermissions);
        } catch (UnsupportedOperationException | IOException e) {
            System.err.println("No se pudieron establecer permisos al directorio: " + dirPath + " -> " + e.getMessage());
        }

        // Escribir archivo
        Files.write(filepath, dto.getFile().getBytes());

        // Establecer permisos 644 al archivo
        try {
            Set<PosixFilePermission> filePermissions = PosixFilePermissions.fromString("rw-r--r--");
            Files.setPosixFilePermissions(filepath, filePermissions);
        } catch (UnsupportedOperationException | IOException e) {
            System.err.println("No se pudieron establecer permisos al archivo: " + filepath + " -> " + e.getMessage());
        }

        dto.getUser().setUriCvFile(filepath.toString());
        UserDto response = userMapper.toDtoFromEntity(userRepository.save(dto.getUser()));
        return response;

    }

    @Override
    public byte[] getCvFile(UserDto user) throws IOException {
        String path = user.getUriCvFile();
        if (path == null || path.isBlank()) {
            throw new FileNotFoundException("No CV file path found for user.");
        }

        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("CV file not found at path: " + path);
        }

        return Files.readAllBytes(filePath);
    }

    @Override
    public void deleteCvFile(UserDto user) throws IOException {
        String path = user.getUriCvFile();
        if (path == null || path.isBlank()) {
            return;
        }

        Path filePath = Paths.get(path);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        Path parentDir = filePath.getParent();
        if (Files.isDirectory(parentDir) && Files.list(parentDir).findAny().isEmpty()) {
            Files.delete(parentDir);
        }

        UserEntity entity = userRepository.findByEmail(user.getEmail())
                        .orElseThrow(() -> new RuntimeException("no found user"));

        entity.setUriCvFile(null);
        userRepository.save(entity);
    }

    @Override
    public void updateCv(CvDto dto) throws IOException {
        UserDto user = userMapper.toDtoFromEntity(dto.getUser());

        deleteCvFile(user);

        Path filepath = Paths.get(path, "cv", dto.getUser().getId(), dto.getName());
        Path dirPath = filepath.getParent();

        Files.createDirectories(dirPath);

        Files.write(filepath, dto.getFile().getBytes());

        try {
            if (Files.getFileStore(filepath).supportsFileAttributeView("posix")) {
                Set<PosixFilePermission> filePermissions = PosixFilePermissions.fromString("rw-r--r--");
                Files.setPosixFilePermissions(filepath, filePermissions);
            }
        } catch (Exception e) {
            System.err.println("Permiso no establecido: " + e.getMessage());
        }

        dto.getUser().setUriCvFile(filepath.toString());
        userRepository.save(dto.getUser());
    }




}
