package com.code_room.user_service.infrastructure.controller;


import com.code_room.user_service.domain.Enum.Role;
import com.code_room.user_service.domain.mapper.userMapper.UserMapper;
import com.code_room.user_service.domain.model.User;
import com.code_room.user_service.domain.ports.UserService;
import com.code_room.user_service.infrastructure.controller.dto.LoginDto;
import com.code_room.user_service.infrastructure.controller.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/user-check")
    public ResponseEntity<?> checkPassword(@RequestBody LoginDto loginDto) {
        try {
            UserDto user = userService.checkPassword(loginDto);
            return ResponseEntity.ok(user);

        }catch (RuntimeException e){
            Map<String, String> error = Map.of("message", e.getMessage(),"code","405");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        catch (Exception e) {
            Map<String, String> error = Map.of("message", "Invalid email or password","code","403");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("code") String code) {
        try {
            userService.verifyUser(code);
            Map<String, String> success = Map.of(
                    "message", "Your account has been successfully verified. You can now log in."
            );
            return ResponseEntity.ok(success);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Verification failed: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserDto dto, @RequestParam String password) {
        try {
            String code = userService.createUser(dto, password);
            Map<String, String> success = Map.of(
                    "message", "User created successfully",
                    "verification code",code
            );
            return ResponseEntity.ok(success);

        } catch (Exception e) {
            Map<String, String> error = Map.of(
                    "message", e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(userMapper.toDtoFromModel(user));

        } catch (Exception e) {
            Map<String, String> error = Map.of(
                    "message", e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(userMapper.toDtoFromModel(user));

        } catch (Exception e) {
            Map<String, String> error = Map.of(
                    "message", e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/role/email/{email}")
    public ResponseEntity<?> getRoleByEmail(@PathVariable String email) {
        try {
            Role role = userService.getRoleByEmail(email);
            return ResponseEntity.ok(role);

        } catch (Exception e) {
            Map<String, String> error = Map.of(
                    "message", e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }


}
