package com.nookly.booking.accommodation.category.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class RequestCategoryDTO {

    @NotBlank(message = "Category name must not be blank")
    @Size(max = 150, message = "Category name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Hotel number must not be blank")
    private String hotelNumber;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be 0 or greater")
    private BigDecimal price;

    @NotNull(message = "Capacity must not be null")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotEmpty(message = "Room numbers list must not be empty")
    private List<@NotNull(message = "Room number must not be null") Integer> roomNumbers;

    private List<@NotNull(message = "Amenity ID must not be null") Long> amenityIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHotelNumber() {
        return hotelNumber;
    }

    public void setHotelNumber(String hotelNumber) {
        this.hotelNumber = hotelNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<Integer> getRoomNumbers() {
        return roomNumbers;
    }

    public void setRoomNumbers(List<Integer> roomNumbers) {
        this.roomNumbers = roomNumbers;
    }

    public List<Long> getAmenityIds() {
        return amenityIds;
    }

    public void setAmenityIds(List<Long> amenityIds) {
        this.amenityIds = amenityIds;
    }
}