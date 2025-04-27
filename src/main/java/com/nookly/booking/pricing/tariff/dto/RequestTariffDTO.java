package com.nookly.booking.pricing.tariff.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class RequestTariffDTO {

    @NotBlank(message = "Tariff name must not be blank")
    @Size(max = 150, message = "Tariff name must not exceed 150 characters")
    private String name;

    @NotNull(message = "Refundable field is required")
    private Boolean isRefundable;

    @NotNull(message = "Cancel days before is required")
    @Min(value = 0, message = "Cancel days before must be 0 or greater")
    private Integer cancelDaysBefore;

    @NotNull(message = "Meal plan ID is required")
    private Long mealPlanId;

    @NotBlank(message = "Hotel number must not be blank")
    private String hotelNumber;

    @NotNull(message = "Meal price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Meal price must be 0 or greater")
    private BigDecimal mealPrice;

    @NotEmpty(message = "At least one category ID must be provided")
    private List<@NotNull(message = "Category ID must not be null") UUID> categoryIds;

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

    public Long getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(Long mealPlanId) {
        this.mealPlanId = mealPlanId;
    }

    public String getHotelNumber() {
        return hotelNumber;
    }

    public void setHotelNumber(String hotelNumber) {
        this.hotelNumber = hotelNumber;
    }

    public BigDecimal getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(BigDecimal mealPrice) {
        this.mealPrice = mealPrice;
    }

    public List<UUID> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<UUID> categoryIds) {
        this.categoryIds = categoryIds;
    }
}