package com.nookly.booking.auth.service;

import com.nookly.booking.auth.dto.AuthLoginDTO;
import com.nookly.booking.auth.dto.AuthRegisterDTO;
import com.nookly.booking.auth.exception.InvalidPasswordException;
import com.nookly.booking.auth.exception.UserExistsException;
import com.nookly.booking.user.dto.UserMapper;
import com.nookly.booking.user.dto.UserResponseDTO;
import com.nookly.booking.user.model.User;
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
        User user = userService.getUserByPhone(authLoginDTO.getPhone()).orElse(null);
        if (user == null) return null;

        boolean matches = bCryptPasswordEncoder.matches(authLoginDTO.getPassword(), user.getPassword());

        if (!matches) throw new InvalidPasswordException("Invalid phone number and/or password");

        return UserMapper.INSTANCE.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO register(AuthRegisterDTO authRegisterDTO) {
        if (userService.existsUserByEmail(authRegisterDTO.getEmail())) throw new UserExistsException("Email already taken");
        if (userService.existsUserByUsername(authRegisterDTO.getUsername())) throw new UserExistsException("Username already taken");

        authRegisterDTO.setPassword(bCryptPasswordEncoder.encode(authRegisterDTO.getPassword()));

        return userService.create(authRegisterDTO);
    }

    @Override
    public void logout() {

    }
}
