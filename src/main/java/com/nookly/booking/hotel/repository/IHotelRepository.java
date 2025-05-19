package com.nookly.booking.hotel.repository;

import com.nookly.booking.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IHotelRepository extends JpaRepository<Hotel, UUID> {
    Optional<Hotel> findByHotelNumber(String hotelNumber);

    boolean existsByHotelNumber(String hotelNumber);

    void deleteByHotelNumber(String hotelNumber);

    @Query("SELECT h FROM Hotel h WHERE " +
            "h.latitude BETWEEN :minLat AND :maxLat AND " +
            "h.longitude BETWEEN :minLon AND :maxLon AND " +
            "h.status = 'OPEN'")
    List<Hotel> findHotelsInBoundingBox(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon
    );
}
