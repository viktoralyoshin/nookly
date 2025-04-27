package com.nookly.booking.user.repository;

import com.nookly.booking.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByUsername(String username);

    boolean existsByEmailOrUsername(String email, String username);
}
