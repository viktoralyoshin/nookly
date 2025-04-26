package com.nookly.booking.hotel.service;

import com.nookly.booking.hotel.dto.CreateHotelDTO;
import com.nookly.booking.hotel.dto.HotelResponseDTO;
import com.nookly.booking.hotel.dto.UpdateHotelDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IHotelService {
    Optional<HotelResponseDTO> getHotelById(String hotelId);

    List<HotelResponseDTO> getAllHotels();

    HotelResponseDTO createHotel(CreateHotelDTO createHotelDTO, UUID ownerId);

    boolean deleteHotel(String hotelId);

    HotelResponseDTO updateHotel(String hotelId, UpdateHotelDTO updateHotelDTO);
}
