package com.nookly.booking.user.service;

import com.nookly.booking.auth.dto.AuthMapper;
import com.nookly.booking.auth.dto.AuthRegisterDTO;
import com.nookly.booking.user.dto.UserMapper;
import com.nookly.booking.user.dto.UserResponseDTO;
import com.nookly.booking.user.model.User;
import com.nookly.booking.user.model.UserRole;
import com.nookly.booking.user.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO create(AuthRegisterDTO authRegisterDTO) {

        User user = AuthMapper.INSTANCE.toUser(authRegisterDTO);
        user.setRole(UserRole.USER);
        user = userRepository.save(user);
        return UserMapper.INSTANCE.toUserResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::toUserResponseDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDTO> getUserById(UUID id) {
        return userRepository.findById(id).map(UserMapper.INSTANCE::toUserResponseDTO);
    }

    @Override
    public boolean deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) return false;

        userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
