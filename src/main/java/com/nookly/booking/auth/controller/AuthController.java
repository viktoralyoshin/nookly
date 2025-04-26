package com.nookly.booking.auth.controller;

import com.nookly.booking.auth.dto.AuthLoginDTO;
import com.nookly.booking.auth.dto.AuthRegisterDTO;
import com.nookly.booking.auth.service.AuthService;
import com.nookly.booking.user.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "API для регистрации и аутентификации")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешная регистрация",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь уже существует",
                    content = @Content(
                            schema = @Schema(
                                    implementation = Map.class,
                                    example = "{\"error\": \"Email already taken\"}"
                            )
                    ))
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthRegisterDTO.class)))
            @RequestBody AuthRegisterDTO authRegisterDTO
    ) {
        UserResponseDTO user = authService.register(authRegisterDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginDTO authLoginDTO) {
        return ResponseEntity.ok(authService.login(authLoginDTO));
    }
}
