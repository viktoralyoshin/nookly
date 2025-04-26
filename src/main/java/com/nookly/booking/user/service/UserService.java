package com.nookly.booking.user.service;

import com.nookly.booking.auth.dto.AuthMapper;
import com.nookly.booking.auth.dto.AuthRegisterDTO;
import com.nookly.booking.user.dto.UserMapper;
import com.nookly.booking.user.dto.UserResponseDTO;
import com.nookly.booking.user.model.User;
import com.nookly.booking.user.model.UserRole;
import com.nookly.booking.user.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final UserMapper userMapper;
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponseDTO create(AuthRegisterDTO authRegisterDTO) {

        User user = AuthMapper.INSTANCE.toUser(authRegisterDTO);
        user.setRole(UserRole.USER);
        user = userRepository.save(user);
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponseDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserResponseDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toUserResponseDTO);
    }

    @Override
    @Transactional
    public boolean deleteUserById(UUID id) {
        if (!userRepository.existsById(id)) return false;

        userRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public boolean existsUserByEmailOrUsername(String email, String username) {
        return userRepository.existsByEmailOrUsername(email, username);
    }
}
