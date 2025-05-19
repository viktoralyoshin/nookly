package com.nookly.booking.accommodation.category.repository;

import com.nookly.booking.accommodation.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ICategoryRepository extends JpaRepository<Category, UUID> {
    @Modifying
    @Query(value = "DELETE FROM categories_amenities WHERE category_id = :categoryId",
            nativeQuery = true)
    void deleteAmenityRelations(@Param("categoryId") UUID categoryId);

    @Modifying
    @Query(value = "DELETE FROM tariff_categories WHERE category_id = :categoryId",
            nativeQuery = true)
    void deleteTariffRelations(@Param("categoryId") UUID categoryId);

    @Modifying
    @Query(value = "DELETE FROM rooms WHERE category_id = :categoryId",
            nativeQuery = true)
    void deleteRooms(@Param("categoryId") UUID categoryId);

    List<Category> findByHotelIdAndCapacity(UUID hotelId, Integer capacity);
}
