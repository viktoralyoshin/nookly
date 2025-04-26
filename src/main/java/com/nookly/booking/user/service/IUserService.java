package com.nookly.booking.user.service;

import com.nookly.booking.auth.dto.AuthRegisterDTO;
import com.nookly.booking.user.dto.UserResponseDTO;
import com.nookly.booking.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    UserResponseDTO create(AuthRegisterDTO authRegisterDTO);
    List<UserResponseDTO> getAllUsers();
    Optional<UserResponseDTO> getUserById(UUID id);
    Optional<User> getUserByPhone(String phone);
    Optional<UserResponseDTO> getUserByUsername(String username);
    boolean deleteUserById(UUID id);

    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
}
