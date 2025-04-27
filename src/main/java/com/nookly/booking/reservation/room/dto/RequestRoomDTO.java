package com.nookly.booking.reservation.room.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class RequestRoomDTO {

    @NotBlank(message = "CCategory ID is required")
    private UUID categoryId;

    @NotBlank(message = "Room number name is required")
    private Integer roomNumber;

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }
}
