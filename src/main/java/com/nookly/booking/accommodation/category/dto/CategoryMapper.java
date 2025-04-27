package com.nookly.booking.accommodation.category.dto;

import com.nookly.booking.accommodation.category.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Category requestToCategory(RequestCategoryDTO requestCategoryDTO);

    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Category updateToCategory(UpdateCategoryDTO updateCategoryDTO);

    ResponseCategoryDTO toResponseCategoryDTO(Category category);
    ResponseCategoryOnlyDTO toResponseCategoryOnlyDTO(Category category);

    ResponseCategoryWithoutTariffDTO toResponseCategoryWithoutTariffDTO(Category category);
}
