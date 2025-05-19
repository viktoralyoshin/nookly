package com.nookly.booking.reservation.room.repository;

import com.nookly.booking.accommodation.category.model.Category;
import com.nookly.booking.reservation.room.dto.ResponseRoomOnlyDTO;
import com.nookly.booking.reservation.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IRoomRepository extends JpaRepository<Room, UUID> {
    UUID id(UUID id);

    List<Room> findByCategoryId(UUID categoryId);

    List<Room> findByHotel_HotelNumber(String hotelHotelNumber);
}
