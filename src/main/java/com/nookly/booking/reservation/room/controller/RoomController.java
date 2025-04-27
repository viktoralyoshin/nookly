package com.nookly.booking.reservation.room.controller;

import com.nookly.booking.reservation.room.dto.RequestRoomDTO;
import com.nookly.booking.reservation.room.dto.ResponseRoomOnlyDTO;
import com.nookly.booking.reservation.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rooms")
@Tag(name = "Room Controller", description = "Операции для работы с номерами размещения")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @Operation(summary = "Получить все номера", description = "Возвращает список всех номеров")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseRoomOnlyDTO.class, type = "array")))
    public ResponseEntity<List<ResponseRoomOnlyDTO>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить номер по ID", description = "Возвращает информацию о номере по его идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Номер найден",
                    content = @Content(schema = @Schema(implementation = ResponseRoomOnlyDTO.class))),
            @ApiResponse(responseCode = "404", description = "Номер не найден")
    })
    public ResponseEntity<ResponseRoomOnlyDTO> getRoomById(
            @Parameter(description = "UUID номера", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        return ResponseEntity.ok(roomService.getRoom(id));
    }

    @PostMapping
    @Operation(summary = "Создать номер", description = "Создает новый номер размещения")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Номер успешно создан",
                    content = @Content(schema = @Schema(implementation = ResponseRoomOnlyDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<ResponseRoomOnlyDTO> createRoom(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания номера",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RequestRoomDTO.class)))
            @Valid @RequestBody RequestRoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.createRoom(roomDTO));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить номер", description = "Частично обновляет информацию о номере")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Номер успешно обновлен",
                    content = @Content(schema = @Schema(implementation = ResponseRoomOnlyDTO.class))),
            @ApiResponse(responseCode = "404", description = "Номер не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<ResponseRoomOnlyDTO> updateRoomById(
            @Parameter(description = "UUID номера", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody RequestRoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить номер", description = "Удаляет номер по его идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Номер успешно удален"),
            @ApiResponse(responseCode = "404", description = "Номер не найден")
    })
    public ResponseEntity<Void> deleteRoomById(
            @Parameter(description = "UUID номера", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }
}