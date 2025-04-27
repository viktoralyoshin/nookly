package com.nookly.booking.accommodation.amenity.controller;

import com.nookly.booking.accommodation.amenity.dto.RequestAmenityDTO;
import com.nookly.booking.accommodation.amenity.dto.ResponseAmenityOnlyDTO;
import com.nookly.booking.accommodation.amenity.service.AmenityService;
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

@RestController
@RequestMapping("/amenities")
@Tag(name = "Amenities Controller", description = "Операции для управления удобствами размещения")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @GetMapping
    @Operation(summary = "Получить все удобства", description = "Возвращает список всех доступных удобств")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseAmenityOnlyDTO.class, type = "array")))
    public ResponseEntity<List<ResponseAmenityOnlyDTO>> getAllAmenities() {
        return ResponseEntity.ok(amenityService.getAllAmenities());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить удобство по ID", description = "Возвращает информацию об удобстве по его идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удобство найдено",
                    content = @Content(schema = @Schema(implementation = ResponseAmenityOnlyDTO.class))),
            @ApiResponse(responseCode = "404", description = "Удобство не найдено")
    })
    public ResponseEntity<ResponseAmenityOnlyDTO> getAmenityById(
            @Parameter(description = "ID удобства", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(amenityService.getAmenityById(id));
    }

    @PostMapping
    @Operation(summary = "Создать удобство", description = "Добавляет новое удобство в систему")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удобство успешно создано",
                    content = @Content(schema = @Schema(implementation = ResponseAmenityOnlyDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<ResponseAmenityOnlyDTO> createAmenity(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания удобства",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RequestAmenityDTO.class)))
            @Valid @RequestBody RequestAmenityDTO amenityDTO) {
        return ResponseEntity.ok(amenityService.createAmenity(amenityDTO));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить удобство", description = "Частично обновляет информацию об удобстве")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удобство успешно обновлено",
                    content = @Content(schema = @Schema(implementation = ResponseAmenityOnlyDTO.class))),
            @ApiResponse(responseCode = "404", description = "Удобство не найдено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<ResponseAmenityOnlyDTO> updateAmenityById(
            @Parameter(description = "ID удобства", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody RequestAmenityDTO amenityDTO) {
        return ResponseEntity.ok(amenityService.updateAmenity(id, amenityDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить удобство", description = "Удаляет удобство из системы по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удобство успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Удобство не найдено")
    })
    public ResponseEntity<Void> deleteAmenityById(
            @Parameter(description = "ID удобства", required = true, example = "1")
            @PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.ok().build();
    }
}