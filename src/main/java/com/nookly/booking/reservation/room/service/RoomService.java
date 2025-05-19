package com.nookly.booking.reservation.room.service;

import com.nookly.booking.exception.DataNotFoundException;
import com.nookly.booking.reservation.room.dto.RequestRoomDTO;
import com.nookly.booking.reservation.room.dto.ResponseRoomOnlyDTO;
import com.nookly.booking.reservation.room.dto.RoomMapper;
import com.nookly.booking.reservation.room.model.Room;
import com.nookly.booking.reservation.room.repository.IRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomService implements IRoomService {
    private final IRoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(IRoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public ResponseRoomOnlyDTO getRoom(UUID roomId) {
        return roomRepository.findById(roomId).map(roomMapper::toResponseRoomOnlyDTO).orElseThrow(() -> new DataNotFoundException("Room not found"));
    }

    @Override
    public List<ResponseRoomOnlyDTO> getAllRooms() {
        return roomRepository.findAll().stream().map(roomMapper::toResponseRoomOnlyDTO).collect(Collectors.toList());
    }

    @Override
    public ResponseRoomOnlyDTO createRoom(RequestRoomDTO room) {
        return roomMapper.toResponseRoomOnlyDTO(roomRepository.save(roomMapper.toRoom(room)));
    }

    @Override
    public ResponseRoomOnlyDTO updateRoom(UUID roomId, RequestRoomDTO roomDTO) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new DataNotFoundException("Room not found"));

        if (roomDTO.getRoomNumber() != null) room.setRoomNumber(roomDTO.getRoomNumber());

        return roomMapper.toResponseRoomOnlyDTO(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(UUID roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new DataNotFoundException("Room not found"));
        roomRepository.delete(room);
    }

    @Override
    public List<ResponseRoomOnlyDTO> getRoomsByCategory(UUID categoryId) {
        return roomRepository.findByCategoryId(categoryId).stream().map(roomMapper::toResponseRoomOnlyDTO).collect(Collectors.toList());
    }

    @Override
    public List<ResponseRoomOnlyDTO> getRoomsByHotel(String hotelNumber) {
        return roomRepository.findByHotel_HotelNumber(hotelNumber).stream().map(roomMapper::toResponseRoomOnlyDTO).collect(Collectors.toList());
    }
}

