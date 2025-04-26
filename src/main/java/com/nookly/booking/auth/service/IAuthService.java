package com.nookly.booking.auth.service;

import com.nookly.booking.auth.dto.AuthLoginDTO;
import com.nookly.booking.auth.dto.AuthRegisterDTO;
import com.nookly.booking.user.dto.UserResponseDTO;

import java.util.Optional;

public interface IAuthService {
    public UserResponseDTO login(AuthLoginDTO authLoginDTO);
    public UserResponseDTO register(AuthRegisterDTO authRegisterDTO);
    public void logout();
}
