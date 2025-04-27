package com.nookly.booking.auth.controller;

import com.nookly.booking.auth.dto.AuthLoginDTO;
import com.nookly.booking.auth.dto.AuthRegisterDTO;
import com.nookly.booking.auth.dto.AuthResponseDTO;
import com.nookly.booking.auth.service.AuthService;
import com.nookly.booking.config.JwtConfig;
import com.nookly.booking.exception.jwt.JwtAuthenticationException;
import com.nookly.booking.security.jwt.JwtProvider;
import com.nookly.booking.user.dto.UserResponseDTO;
import com.nookly.booking.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "API для регистрации и аутентификации")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final JwtConfig jwtConfig;
    private final UserService userService;

    public AuthController(AuthService authService, JwtProvider jwtProvider, JwtConfig jwtConfig, UserService userService) {
        this.authService = authService;
        this.jwtProvider = jwtProvider;
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    private record Tokens(String accessToken, String refreshToken) {
    }

    private Tokens generateTokens(String username, Long accessTokenExpiresIn, Long refreshExpiresIn) {
        String accessToken = jwtProvider.generateToken(username, accessTokenExpiresIn);
        String refreshToken = jwtProvider.generateToken(username, refreshExpiresIn);

        return new Tokens(accessToken, refreshToken);
    }

    private void addRefreshTokenToResponse(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh-token", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setMaxAge(24 * 60 * 60);

        response.addCookie(refreshTokenCookie);
    }

    private void removeRefreshTokenFromResponse(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refresh-token", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);
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
                                    example = "{\"Error\": \"Email already taken\"}"
                            )
                    ))
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthRegisterDTO.class)))
            @Valid @RequestBody AuthRegisterDTO authRegisterDTO,
            HttpServletResponse response
    ) {
        UserResponseDTO user = authService.register(authRegisterDTO);

        Tokens tokens = generateTokens(user.getUsername(), jwtConfig.getAccessExpiration(), jwtConfig.getRefreshExpiration());
        addRefreshTokenToResponse(response, tokens.refreshToken);

        return ResponseEntity.ok(new AuthResponseDTO(user, tokens.accessToken));
    }

    @Operation(summary = "Вход в аккаунт пользователем")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь с таким номером телефона не найден"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неправильный пароль и/или логин",
                    content = @Content(
                            schema = @Schema(
                                    implementation = Map.class,
                                    example = "{\"Error\": \"Invalid phone number and/or password\"}"
                            )
                    ))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthLoginDTO.class)))
            @Valid @RequestBody AuthLoginDTO authLoginDTO,
            HttpServletResponse response
    ) {
        UserResponseDTO user = authService.login(authLoginDTO);
        if (user == null) return ResponseEntity.notFound().build();

        Tokens tokens = generateTokens(user.getUsername(), jwtConfig.getAccessExpiration(), jwtConfig.getRefreshExpiration());

        addRefreshTokenToResponse(response, tokens.refreshToken);

        return ResponseEntity.ok(new AuthResponseDTO(user, tokens.accessToken));
    }

    @Operation(summary = "Получение новой пары токенов")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Токены обновлены успешны",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка при обновлении",
                    content = @Content(
                            schema = @Schema(
                                    implementation = Map.class,
                                    example = "{\"Error\": \"Failed to refresh tokens\"}"
                            )
                    )),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не валидный Refresh Token",
                    content = @Content(
                            schema = @Schema(
                                    implementation = Map.class,
                                    example = "{\"Error\": \"Jwt exception\"}"
                            )
                    ))
    })
    @GetMapping("/token")
    public ResponseEntity<AuthResponseDTO> getNewTokens(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshTokenFromCookies = extractRefreshTokenFromCookies(request);
            if (refreshTokenFromCookies != null && jwtProvider.validateToken(refreshTokenFromCookies)) {
                String username = jwtProvider.getUsernameFromToken(refreshTokenFromCookies);

                UserResponseDTO user = userService.getUserByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

                Tokens tokens = generateTokens(username, jwtConfig.getAccessExpiration(), jwtConfig.getRefreshExpiration());

                addRefreshTokenToResponse(response, tokens.refreshToken);

                return ResponseEntity.ok(new AuthResponseDTO(user, tokens.accessToken));
            }
        } catch (JwtAuthenticationException e) {
            removeRefreshTokenFromResponse(response);
            throw new JwtAuthenticationException(e.getMessage(), e);
        } catch (Exception e) {
            removeRefreshTokenFromResponse(response);
            throw new JwtAuthenticationException("Failed to refresh tokens", e);
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Выход из аккаунта")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Выход прошёл успешно"
            )
    })
    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        removeRefreshTokenFromResponse(response);
    }

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh-token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
