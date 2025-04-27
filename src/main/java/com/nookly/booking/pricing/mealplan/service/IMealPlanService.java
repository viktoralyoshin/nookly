package com.nookly.booking.pricing.mealplan.service;

import com.nookly.booking.pricing.mealplan.dto.RequestMealPlanDTO;
import com.nookly.booking.pricing.mealplan.dto.ResponseMealPlanOnlyDTO;

import java.util.List;

public interface IMealPlanService {
    ResponseMealPlanOnlyDTO getMealPlanById(Long id);

    List<ResponseMealPlanOnlyDTO> getAllMealPlans();

    ResponseMealPlanOnlyDTO createMealPlan(RequestMealPlanDTO requestMealPlanDTO);

    ResponseMealPlanOnlyDTO updateMealPlan(Long id, RequestMealPlanDTO requestMealPlanDTO);

    boolean deleteMealPlan(Long id);
}
