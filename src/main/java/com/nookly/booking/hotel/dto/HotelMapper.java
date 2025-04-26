package com.nookly.booking.hotel.dto;

import com.nookly.booking.hotel.model.Hotel;
import com.nookly.booking.user.dto.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HotelMapper {
    Hotel toHotelFromCreatHotelDTO(CreateHotelDTO createHotelDTO);
    HotelResponseDTO toHotelResponseDTO(Hotel hotel);
    Hotel toHotelFromUpdateHotelDTO(UpdateHotelDTO updateHotelDTO);
}
