package com.nookly.booking.pricing.mealplan.dto;

import com.nookly.booking.pricing.mealplan.model.MealPlan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MealPlanMapper {
    MealPlan toMealPlan(RequestMealPlanDTO createMealPlanDTO);

    ResponseMealPlanOnlyDTO toResponseMealPlanOnlyDTO(MealPlan mealPlan);
}
