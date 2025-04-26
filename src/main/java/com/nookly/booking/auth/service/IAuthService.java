package com.nookly.booking.auth.service;

import com.nookly.booking.auth.dto.AuthLoginDTO;
import com.nookly.booking.auth.dto.AuthRegisterDTO;
import com.nookly.booking.user.dto.UserResponseDTO;

public interface IAuthService {
    UserResponseDTO login(AuthLoginDTO authLoginDTO);

    UserResponseDTO register(AuthRegisterDTO authRegisterDTO);
}
