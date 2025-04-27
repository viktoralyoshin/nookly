package com.nookly.booking.accommodation.category.controller;

import com.nookly.booking.accommodation.category.dto.RequestCategoryDTO;
import com.nookly.booking.accommodation.category.dto.ResponseCategoryDTO;
import com.nookly.booking.accommodation.category.dto.UpdateCategoryDTO;
import com.nookly.booking.accommodation.category.model.Category;
import com.nookly.booking.accommodation.category.service.ICategoryService;
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
@RequestMapping("/categories")
@Tag(name = "Category Controller", description = "Управление категориями номеров (например, 'Стандарт', 'Люкс')")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Получить все категории", description = "Возвращает список всех категорий размещения")
    @ApiResponse(responseCode = "200", description = "Успешный запрос",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Category.class, type = "array")))
    public ResponseEntity<List<ResponseCategoryDTO>> getAllCategories() {
        List<ResponseCategoryDTO> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить категорию по ID", description = "Возвращает категорию по её UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория найдена",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    public ResponseEntity<ResponseCategoryDTO> getCategoryById(
            @Parameter(description = "UUID категории", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable("id") UUID id) {
        ResponseCategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @Operation(summary = "Создать категорию", description = "Создаёт новую категорию размещения")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория успешно создана",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
    })
    public ResponseEntity<ResponseCategoryDTO> createCategory(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания категории",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RequestCategoryDTO.class)))
            @Valid @RequestBody RequestCategoryDTO requestCategoryDTO) {
        ResponseCategoryDTO category = categoryService.createCategory(requestCategoryDTO);
        return ResponseEntity.ok(category);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить категорию", description = "Частично обновляет существующую категорию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория обновлена",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
    })
    public ResponseEntity<ResponseCategoryDTO> updateCategory(
            @Parameter(description = "UUID категории", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO) {
        ResponseCategoryDTO category = categoryService.updateCategory(id, updateCategoryDTO);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить категорию", description = "Удаляет категорию по её UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "UUID категории", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable("id") UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}