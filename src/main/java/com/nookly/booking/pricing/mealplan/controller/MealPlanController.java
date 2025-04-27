package com.nookly.booking.pricing.mealplan.controller;

import com.nookly.booking.pricing.mealplan.dto.RequestMealPlanDTO;
import com.nookly.booking.pricing.mealplan.dto.ResponseMealPlanOnlyDTO;
import com.nookly.booking.pricing.mealplan.model.MealPlan;
import com.nookly.booking.pricing.mealplan.service.MealPlanService;
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
@RequestMapping("/meal-plans")
@Tag(name = "Meal Plan Controller", description = "Управление планами питания (например, 'Завтрак', 'Все включено')")
public class MealPlanController {
    private final MealPlanService mealPlanService;

    public MealPlanController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    @PostMapping
    @Operation(summary = "Создать план питания", description = "Создает новый план питания")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "План питания успешно создан", content = @Content(schema = @Schema(implementation = MealPlan.class))), @ApiResponse(responseCode = "400", description = "Некорректные входные данные")})
    public ResponseEntity<ResponseMealPlanOnlyDTO> createMealPlan(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания плана питания", required = true, content = @Content(schema = @Schema(implementation = RequestMealPlanDTO.class))) @Valid @RequestBody RequestMealPlanDTO mealPlanDTO) {
        ResponseMealPlanOnlyDTO mealPlan = mealPlanService.createMealPlan(mealPlanDTO);
        return ResponseEntity.ok(mealPlan);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить план питания", description = "Частично обновляет существующий план питания")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "План питания успешно обновлен", content = @Content(schema = @Schema(implementation = MealPlan.class))), @ApiResponse(responseCode = "404", description = "План питания не найден"), @ApiResponse(responseCode = "400", description = "Некорректные входные данные")})
    public ResponseEntity<ResponseMealPlanOnlyDTO> updateMealPlan(@Parameter(description = "ID плана питания", required = true, example = "1") @PathVariable Long id, @Valid @RequestBody RequestMealPlanDTO mealPlanDTO) {
        ResponseMealPlanOnlyDTO mealPlan = mealPlanService.updateMealPlan(id, mealPlanDTO);
        return ResponseEntity.ok(mealPlan);
    }

    @GetMapping
    @Operation(summary = "Получить все планы питания", description = "Возвращает список всех планов питания")
    @ApiResponse(responseCode = "200", description = "Успешный запрос", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MealPlan.class, type = "array")))
    public ResponseEntity<List<ResponseMealPlanOnlyDTO>> getMealPlans() {
        List<ResponseMealPlanOnlyDTO> mealPlans = mealPlanService.getAllMealPlans();
        return ResponseEntity.ok(mealPlans);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить план питания по ID", description = "Возвращает план питания по его идентификатору")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "План питания найден", content = @Content(schema = @Schema(implementation = MealPlan.class))), @ApiResponse(responseCode = "404", description = "План питания не найден")})
    public ResponseEntity<ResponseMealPlanOnlyDTO> getMealPlan(@Parameter(description = "ID плана питания", required = true, example = "1") @PathVariable Long id) {
        ResponseMealPlanOnlyDTO mealPlan = mealPlanService.getMealPlanById(id);
        return ResponseEntity.ok(mealPlan);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить план питания", description = "Удаляет план питания по его идентификатору")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "План питания успешно удален", content = @Content(schema = @Schema(example = "План питания успешно удален"))), @ApiResponse(responseCode = "404", description = "План питания не найден")})
    public ResponseEntity<String> deleteMealPlan(@Parameter(description = "ID плана питания", required = true, example = "1") @PathVariable Long id) {
        boolean result = mealPlanService.deleteMealPlan(id);
        return ResponseEntity.ok(result ? "План питания успешно удален" : "План питания не найден");
    }
}