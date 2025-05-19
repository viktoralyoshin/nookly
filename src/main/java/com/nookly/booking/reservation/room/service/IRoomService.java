package com.nookly.booking.reservation.room.service;

import com.nookly.booking.reservation.room.dto.RequestRoomDTO;
import com.nookly.booking.reservation.room.dto.ResponseRoomOnlyDTO;

import java.util.List;
import java.util.UUID;

public interface IRoomService {
    ResponseRoomOnlyDTO getRoom(UUID roomId);
    List<ResponseRoomOnlyDTO> getAllRooms();
    ResponseRoomOnlyDTO createRoom(RequestRoomDTO roomDTO);
    ResponseRoomOnlyDTO updateRoom(UUID roomId, RequestRoomDTO roomDTO);
    void deleteRoom(UUID roomId);
    List<ResponseRoomOnlyDTO> getRoomsByCategory(UUID categoryId);
    List<ResponseRoomOnlyDTO> getRoomsByHotel(String hotelNumber);
}
