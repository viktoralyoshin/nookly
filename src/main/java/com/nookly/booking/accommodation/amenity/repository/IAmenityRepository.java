package com.nookly.booking.accommodation.amenity.repository;

import com.nookly.booking.accommodation.amenity.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAmenityRepository extends JpaRepository<Amenity, Long> {
}
