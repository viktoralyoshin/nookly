package com.nookly.booking.accommodation.category.dto;

import com.nookly.booking.accommodation.amenity.dto.ResponseAmenityOnlyDTO;
import com.nookly.booking.reservation.room.dto.ResponseRoomOnlyDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ResponseCategoryWithoutTariffDTO {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Integer capacity;
    private List<ResponseRoomOnlyDTO> rooms;
    private List<ResponseAmenityOnlyDTO> amenities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<ResponseRoomOnlyDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<ResponseRoomOnlyDTO> rooms) {
        this.rooms = rooms;
    }

    public List<ResponseAmenityOnlyDTO> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<ResponseAmenityOnlyDTO> amenities) {
        this.amenities = amenities;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
