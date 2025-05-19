package com.nookly.booking.reservation.room_block.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SearchDTO {
    @NotNull(message = "Latitude can't be null")
    private double latitude;
    @NotNull(message = "Longitude can't be null")
    private double longitude;
    @NotNull(message = "Capacity can't be null")
    private Integer capacity;
    @NotNull(message = "Check In can't be null")
    private LocalDate checkIn;
    @NotNull(message = "Check Out can't be null")
    private LocalDate checkOut;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
}
