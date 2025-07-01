package com.code_room.user_service.infrastructure.controller;

import com.code_room.user_service.domain.ports.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

@RestController
@RequestMapping("/cv")
public class CvController {

    @Autowired
    private CvService cvService;

    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadCv(@RequestParam("file") MultipartFile multipartFile,
                                      @PathVariable("userId") String userId) {
        try {
            cvService.uploadUriCvFile(multipartFile, userId);

            return ResponseEntity.ok("CV uploaded successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading CV.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> downloadCv(@PathVariable("userId") String userId) {
        try {
            byte[] fileContent = cvService.getCvFile(userId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"cv.pdf\"")
                    .header("Content-Type", "application/pdf")
                    .body(fileContent);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CV not found for this user.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving CV.");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteCv(@PathVariable("userId") String userId) {
        try {
            cvService.deleteCvFile(userId);
            return ResponseEntity.ok("CV deleted successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting CV.");
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateCv(@RequestParam("file") MultipartFile multipartFile,
                                      @PathVariable("userId") String userId) {
        try {
            cvService.updateCvFile(multipartFile, userId);
            return ResponseEntity.ok("CV updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating CV.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

}
