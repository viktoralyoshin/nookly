package com.nookly.booking.auth.service;

import com.nookly.booking.auth.dto.AuthLoginDTO;
import com.nookly.booking.auth.dto.AuthRegisterDTO;
import com.nookly.booking.auth.exception.UserExistsException;
import com.nookly.booking.user.dto.UserResponseDTO;
import com.nookly.booking.user.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public AuthService(UserService userService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserResponseDTO login(AuthLoginDTO authLoginDTO) {
        return null;
    }

    @Override
    public UserResponseDTO register(AuthRegisterDTO authRegisterDTO) {
        if (userService.existsUserByEmail(authRegisterDTO.getEmail())) throw new UserExistsException("Email already taken");
        if (userService.existsUserByUsername(authRegisterDTO.getUsername())) throw new UserExistsException("Username already taken");

        authRegisterDTO.setPassword(bCryptPasswordEncoder.encode(authRegisterDTO.getPassword()));

        return userService.create(authRegisterDTO);
    }
}
