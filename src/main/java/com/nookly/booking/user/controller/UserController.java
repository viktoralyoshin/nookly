package com.nookly.booking.user.controller;

import com.nookly.booking.user.dto.UserMapper;
import com.nookly.booking.user.dto.UserResponseDTO;
import com.nookly.booking.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Управление пользователями")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Получение всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи найдены, либо пустой массив")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "UUID пользователя")
            @PathVariable UUID id
    ) {
        UserResponseDTO userResponseDTO = userService.getUserById(id).map(userMapper::toUserResponseDTO).orElse(null);
        if (userResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(summary = "Удаление пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(
            @Parameter(description = "UUID пользователя")
            @PathVariable UUID id
    ) {
        boolean rename = userService.deleteUserById(id);
        return rename ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
