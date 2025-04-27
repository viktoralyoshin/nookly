package com.nookly.booking.pricing.tariff.dto;

import com.nookly.booking.pricing.mealplan.dto.ResponseMealPlanOnlyDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ResponseTariffOnlyDTO {
    private UUID id;
    private String name;
    private Boolean isRefundable;
    private Integer cancelDaysBefore;
    private ResponseMealPlanOnlyDTO mealPlan;
    private BigDecimal mealPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRefundable() {
        return isRefundable;
    }

    public void setRefundable(Boolean refundable) {
        isRefundable = refundable;
    }

    public Integer getCancelDaysBefore() {
        return cancelDaysBefore;
    }

    public void setCancelDaysBefore(Integer cancelDaysBefore) {
        this.cancelDaysBefore = cancelDaysBefore;
    }

    public ResponseMealPlanOnlyDTO getMealPlan() {
        return mealPlan;
    }

    public void setMealPlan(ResponseMealPlanOnlyDTO mealPlan) {
        this.mealPlan = mealPlan;
    }

    public BigDecimal getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(BigDecimal mealPrice) {
        this.mealPrice = mealPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
