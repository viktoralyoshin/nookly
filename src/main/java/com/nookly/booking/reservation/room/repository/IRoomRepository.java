package com.nookly.booking.reservation.room.repository;

import com.nookly.booking.reservation.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IRoomRepository extends JpaRepository<Room, UUID> {
    UUID id(UUID id);
}
