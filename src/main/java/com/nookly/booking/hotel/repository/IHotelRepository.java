package com.nookly.booking.hotel.repository;

import com.nookly.booking.hotel.model.Hotel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IHotelRepository extends JpaRepository<Hotel, UUID> {
    Optional<Hotel> findByHotelNumber(String hotelNumber);

    boolean existsByHotelNumber(String hotelNumber);

    void deleteByHotelNumber(String hotelNumber);
}
