package com.code_room.user_service.infrastructure.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginDto {
    private String email;
    private String password;
}
