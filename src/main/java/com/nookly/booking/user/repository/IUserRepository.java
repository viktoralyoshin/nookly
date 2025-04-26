package com.nookly.booking.user.repository;

import com.nookly.booking.user.dto.UserResponseDTO;
import com.nookly.booking.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findByPhone(String phone);

    Optional<User> findByUsername(String username);
}
