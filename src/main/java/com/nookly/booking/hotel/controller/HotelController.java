package com.nookly.booking.hotel.controller;

import com.nookly.booking.annotation.CurrentUser;
import com.nookly.booking.hotel.dto.CreateHotelDTO;
import com.nookly.booking.hotel.dto.HotelResponseDTO;
import com.nookly.booking.hotel.dto.UpdateHotelDTO;
import com.nookly.booking.hotel.service.HotelService;
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
@RequestMapping("/hotels")
@Tag(name = "Hotel Сontroller", description = "API для управления отелями")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отель по ID", description = "Возвращает детальную информацию об отеле")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отель найден",
                    content = @Content(schema = @Schema(implementation = HotelResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public ResponseEntity<HotelResponseDTO> getHotel(
            @Parameter(description = "ID отеля", required = true, example = "12345")
            @PathVariable String id
    ) {
        return hotelService.getHotelById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Получить список всех отелей", description = "Возвращает список всех доступных отелей")
    @ApiResponse(responseCode = "200", description = "Список отелей получен",
            content = @Content(schema = @Schema(implementation = HotelResponseDTO.class)))
    public ResponseEntity<List<HotelResponseDTO>> getHotels() {
        List<HotelResponseDTO> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @PostMapping
    @Operation(summary = "Создать новый отель", description = "Создает новый отель и возвращает его данные")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отель успешно создан",
                    content = @Content(schema = @Schema(implementation = HotelResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные данные отеля"),
            @ApiResponse(responseCode = "401", description = "Требуется аутентификация")
    })
    public ResponseEntity<HotelResponseDTO> createHotel(
            @Parameter(description = "ID текущего пользователя", hidden = true)
            @CurrentUser UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания отеля",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateHotelDTO.class)))
            @Valid @RequestBody CreateHotelDTO createHotelDTO
    ) {
        HotelResponseDTO hotel = hotelService.createHotel(createHotelDTO, id);
        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отель", description = "Удаляет отель по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отель успешно удален"),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public ResponseEntity<String> deleteHotel(
            @Parameter(description = "ID отеля для удаления", required = true, example = "12345")
            @PathVariable String id
    ) {
        boolean result = hotelService.deleteHotel(id);
        return result ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить данные отеля",
            description = "Частично обновляет данные отеля. Можно передавать только изменяемые поля.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные отеля успешно обновлены",
                    content = @Content(schema = @Schema(implementation = HotelResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "404", description = "Отель не найден")
    })
    public ResponseEntity<HotelResponseDTO> updateHotel(@Parameter(description = "ID отеля", required = true, example = "21234567")
                                                        @PathVariable String id,
                                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                description = "Данные для обновления отеля",
                                                                required = true,
                                                                content = @Content(schema = @Schema(implementation = UpdateHotelDTO.class)))
                                                        @Valid @RequestBody UpdateHotelDTO updateHotelDTO
    ) {
        HotelResponseDTO hotel = hotelService.updateHotel(id, updateHotelDTO);
        return ResponseEntity.ok(hotel);
    }
}