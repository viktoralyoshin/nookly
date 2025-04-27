package com.nookly.booking.pricing.mealplan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RequestMealPlanDTO {
    @NotBlank(message = "Meal plan name is required")
    @Size(min = 2, max = 100, message = "Meal plan name must be between 2 and 100 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
