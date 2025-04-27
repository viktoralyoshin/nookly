package com.nookly.booking.accommodation.category.service;

import com.nookly.booking.accommodation.category.dto.RequestCategoryDTO;
import com.nookly.booking.accommodation.category.dto.ResponseCategoryDTO;
import com.nookly.booking.accommodation.category.dto.UpdateCategoryDTO;

import java.util.List;
import java.util.UUID;

public interface ICategoryService {
    ResponseCategoryDTO getCategoryById(UUID id);
    List<ResponseCategoryDTO> getCategories();
    ResponseCategoryDTO createCategory(RequestCategoryDTO requestCategoryDTO);
    ResponseCategoryDTO updateCategory(UUID id, UpdateCategoryDTO updateCategoryDTO);
    void deleteCategory(UUID id);
}
