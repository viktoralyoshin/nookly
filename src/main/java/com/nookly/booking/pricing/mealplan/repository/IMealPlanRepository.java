package com.nookly.booking.pricing.mealplan.repository;

import com.nookly.booking.pricing.mealplan.model.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMealPlanRepository extends JpaRepository<MealPlan, Long> {
}
