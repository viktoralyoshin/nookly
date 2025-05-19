package com.nookly.booking.reservation.room_block.service;

import com.nookly.booking.accommodation.category.dto.ResponseCategoryOnlyDTO;
import com.nookly.booking.hotel.dto.HotelResponseDTO;
import com.nookly.booking.hotel.model.Hotel;
import com.nookly.booking.reservation.room_block.dto.BlockDayDTO;
import com.nookly.booking.reservation.room_block.dto.BlockPeriodDTO;
import com.nookly.booking.reservation.room_block.dto.SearchDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IRoomAvailabilityService {
    List<ResponseCategoryOnlyDTO> getAvailableCategories(String hotelNumber, LocalDate checkIn, LocalDate checkOut, Integer capacity);
    List<HotelResponseDTO> getAvailableHotels(SearchDTO search);
    void blockRoomForPeriod(UUID roomID, BlockPeriodDTO blockPeriodDTO);
    void blockRoomForDay(UUID roomID, BlockDayDTO blockDayDTO);
    void unblockRoomForPeriod(UUID roomId, BlockPeriodDTO blockPeriodDTO);
    void unblockRoomForDay(UUID roomId, BlockDayDTO blockDayDTO);
}
