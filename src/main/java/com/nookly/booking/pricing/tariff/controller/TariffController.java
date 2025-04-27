package com.nookly.booking.pricing.tariff.controller;

import com.nookly.booking.pricing.tariff.dto.RequestTariffDTO;
import com.nookly.booking.pricing.tariff.dto.ResponseTariffDTO;
import com.nookly.booking.pricing.tariff.dto.UpdateTariffDTO;
import com.nookly.booking.pricing.tariff.service.TariffService;
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
@RequestMapping("/tariffs")
@Tag(name = "Tariff Controller", description = "Операции для управления тарифами размещения")
public class TariffController {
    private final TariffService tariffService;

    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @GetMapping
    @Operation(summary = "Получить все тарифы", description = "Возвращает список всех доступных тарифов")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseTariffDTO.class, type = "array")))
    public ResponseEntity<List<ResponseTariffDTO>> getTariffs() {
        return ResponseEntity.ok(tariffService.getTariffs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тариф по ID", description = "Возвращает информацию о тарифе по его идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тариф найден",
                    content = @Content(schema = @Schema(implementation = ResponseTariffDTO.class))),
            @ApiResponse(responseCode = "404", description = "Тариф не найден")
    })
    public ResponseEntity<ResponseTariffDTO> getTariff(
            @Parameter(description = "UUID тарифа", required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        return ResponseEntity.ok(tariffService.getTariffById(id));
    }

    @PostMapping
    @Operation(summary = "Создать тариф", description = "Добавляет новый тариф в систему")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тариф успешно создан",
                    content = @Content(schema = @Schema(implementation = ResponseTariffDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
    })
    public ResponseEntity<ResponseTariffDTO> createTariff(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания тарифа",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RequestTariffDTO.class)))
            @Valid @RequestBody RequestTariffDTO requestTariffDTO) {
        return ResponseEntity.ok(tariffService.createTariff(requestTariffDTO));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить тариф", description = "Частично обновляет информацию о тарифе")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тариф успешно обновлен",
                    content = @Content(schema = @Schema(implementation = ResponseTariffDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Тариф не найден"),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
    })
    public ResponseEntity<ResponseTariffDTO> updateTariff(
            @Parameter(description = "UUID тарифа", required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTariffDTO updateTariffDTO) {
        return ResponseEntity.ok(tariffService.updateTariff(id, updateTariffDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить тариф", description = "Удаляет тариф из системы по его ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тариф успешно удален"),
            @ApiResponse(responseCode = "404", description = "Тариф не найден")
    })
    public ResponseEntity<Void> deleteTariff(
            @Parameter(description = "UUID тарифа", required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        tariffService.deleteTariff(id);
        return ResponseEntity.ok().build();
    }
}