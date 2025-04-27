package com.nookly.booking.reservation.room.dto;

import com.nookly.booking.reservation.room.model.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room toRoom(RequestRoomDTO requestRoomDTO);

    ResponseRoomOnlyDTO toResponseRoomOnlyDTO(Room room);
}
