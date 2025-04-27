package com.nookly.booking.pricing.mealplan.service;

import com.nookly.booking.exception.DataNotFoundException;
import com.nookly.booking.pricing.mealplan.dto.MealPlanMapper;
import com.nookly.booking.pricing.mealplan.dto.RequestMealPlanDTO;
import com.nookly.booking.pricing.mealplan.dto.ResponseMealPlanOnlyDTO;
import com.nookly.booking.pricing.mealplan.model.MealPlan;
import com.nookly.booking.pricing.mealplan.repository.IMealPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealPlanService implements IMealPlanService {

    private final IMealPlanRepository mealPlanRepository;
    private final MealPlanMapper mealPlanMapper;

    public MealPlanService(IMealPlanRepository mealPlanRepository, MealPlanMapper mealPlanMapper) {
        this.mealPlanRepository = mealPlanRepository;
        this.mealPlanMapper = mealPlanMapper;
    }

    @Override
    public ResponseMealPlanOnlyDTO getMealPlanById(Long id) {
        return mealPlanRepository.findById(id).map(mealPlanMapper::toResponseMealPlanOnlyDTO).orElseThrow(() -> new DataNotFoundException("MealPlan not found"));
    }

    @Override
    public List<ResponseMealPlanOnlyDTO> getAllMealPlans() {
        return mealPlanRepository.findAll().stream().map(mealPlanMapper::toResponseMealPlanOnlyDTO).collect(Collectors.toList());
    }

    @Override
    public ResponseMealPlanOnlyDTO createMealPlan(RequestMealPlanDTO requestMealPlanDTO) {
        return mealPlanMapper.toResponseMealPlanOnlyDTO(mealPlanRepository.save(mealPlanMapper.toMealPlan(requestMealPlanDTO)));
    }

    @Override
    public ResponseMealPlanOnlyDTO updateMealPlan(Long id, RequestMealPlanDTO requestMealPlanDTO) {
        MealPlan mealPlan = mealPlanRepository.findById(id).orElseThrow(() -> new DataNotFoundException("MealPlan not found"));

        if (requestMealPlanDTO.getName() != null) mealPlan.setName(requestMealPlanDTO.getName());

        return mealPlanMapper.toResponseMealPlanOnlyDTO(mealPlanRepository.save(mealPlan));
    }

    @Override
    public boolean deleteMealPlan(Long id) {
        if (!mealPlanRepository.existsById(id)) return false;
        mealPlanRepository.deleteById(id);
        return true;
    }
}
