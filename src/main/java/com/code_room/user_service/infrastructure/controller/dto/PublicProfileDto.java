package com.code_room.user_service.infrastructure.controller.dto;

import lombok.Data;
import java.util.List;

@Data
public class PublicProfileDto {
    private String summary;
    private List<String> skills;
    private List<String> experienceHighlights;
}