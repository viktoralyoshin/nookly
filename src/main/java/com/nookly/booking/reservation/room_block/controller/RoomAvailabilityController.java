package com.nookly.booking.reservation.room_block.controller;

import com.nookly.booking.accommodation.category.dto.ResponseCategoryOnlyDTO;
import com.nookly.booking.hotel.dto.HotelResponseDTO;
import com.nookly.booking.reservation.room_block.dto.BlockDayDTO;
import com.nookly.booking.reservation.room_block.dto.BlockPeriodDTO;
import com.nookly.booking.reservation.room_block.dto.SearchDTO;
import com.nookly.booking.reservation.room_block.service.IRoomAvailabilityService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/available")
public class RoomAvailabilityController {
    private final IRoomAvailabilityService roomAvailabilityService;

    public RoomAvailabilityController(IRoomAvailabilityService roomAvailabilityService) {
        this.roomAvailabilityService = roomAvailabilityService;
    }

    @PostMapping("/block-period/{roomID}")
    public ResponseEntity<Boolean> blockRoomForPeriod(
            @RequestBody @Valid BlockPeriodDTO blockPeriodDTO,
            @PathVariable UUID roomID
    ) {
        roomAvailabilityService.blockRoomForPeriod(roomID, blockPeriodDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block-day/{roomID}")
    public ResponseEntity<Boolean> blockRoomForDay(
            @RequestBody @Valid BlockDayDTO blockDayDTO, @PathVariable UUID roomID
    ) {
        roomAvailabilityService.blockRoomForDay(roomID, blockDayDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/block-period/{roomID}")
    public ResponseEntity<Boolean> deleteBlockPeriod(
            @RequestBody @Valid BlockPeriodDTO blockPeriodDTO,
            @PathVariable UUID roomID
    ) {
        roomAvailabilityService.unblockRoomForPeriod(roomID, blockPeriodDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/block-day/{roomID}")
    public ResponseEntity<Boolean> deleteBlockDay(
            @RequestBody @Valid BlockDayDTO blockDayDTO,
            @PathVariable UUID roomID) {
        roomAvailabilityService.unblockRoomForDay(roomID, blockDayDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hotel")
    public ResponseEntity<List<ResponseCategoryOnlyDTO>> getAvailableCategories(
            @RequestParam String hotelNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate checkOut,
            @RequestParam Integer capacity
    ) {
        List<ResponseCategoryOnlyDTO> availableCategories = roomAvailabilityService.getAvailableCategories(hotelNumber, checkIn, checkOut, capacity);

        return ResponseEntity.ok(availableCategories);
    }

    @PostMapping("/search")
    public ResponseEntity<List<HotelResponseDTO>> getAvailableHotels(
            @Valid @RequestBody SearchDTO searchDTO
    ) {
        return ResponseEntity.ok(roomAvailabilityService.getAvailableHotels(searchDTO));
    }
}
