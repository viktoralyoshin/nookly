package com.nookly.booking.pricing.tariff.service;

import com.nookly.booking.accommodation.category.model.Category;
import com.nookly.booking.accommodation.category.repository.ICategoryRepository;
import com.nookly.booking.exception.DataNotFoundException;
import com.nookly.booking.hotel.model.Hotel;
import com.nookly.booking.hotel.repository.IHotelRepository;
import com.nookly.booking.pricing.mealplan.model.MealPlan;
import com.nookly.booking.pricing.mealplan.repository.IMealPlanRepository;
import com.nookly.booking.pricing.tariff.dto.RequestTariffDTO;
import com.nookly.booking.pricing.tariff.dto.ResponseTariffDTO;
import com.nookly.booking.pricing.tariff.dto.TariffMapper;
import com.nookly.booking.pricing.tariff.dto.UpdateTariffDTO;
import com.nookly.booking.pricing.tariff.model.Tariff;
import com.nookly.booking.pricing.tariff.repository.ITariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TariffService implements ITariffService {
    private final ITariffRepository tariffRepository;
    private final IHotelRepository hotelRepository;
    private final ICategoryRepository categoryRepository;
    private final IMealPlanRepository mealPlanRepository;
    private final TariffMapper tariffMapper;

    public TariffService(ITariffRepository tariffRepository,
                         IHotelRepository hotelRepository,
                         ICategoryRepository categoryRepository,
                         IMealPlanRepository mealPlanRepository,
                         TariffMapper tariffMapper) {
        this.tariffRepository = tariffRepository;
        this.hotelRepository = hotelRepository;
        this.categoryRepository = categoryRepository;
        this.mealPlanRepository = mealPlanRepository;
        this.tariffMapper = tariffMapper;
    }

    @Override
    public List<ResponseTariffDTO> getTariffs() {
        return tariffRepository.findAll().stream().map(tariffMapper::toResponseTariffDTO).collect(Collectors.toList());
    }

    @Override
    public ResponseTariffDTO getTariffById(UUID id) {
        return tariffRepository.findById(id).map(tariffMapper::toResponseTariffDTO).orElseThrow(() -> new DataNotFoundException("Tariff not found"));
    }

    @Override
    @Transactional
    public ResponseTariffDTO createTariff(RequestTariffDTO requestTariffDTO) {
        Hotel hotel = hotelRepository.findByHotelNumber(requestTariffDTO.getHotelNumber()).orElseThrow(() -> new DataNotFoundException("Hotel not found"));
        MealPlan mealPlan = mealPlanRepository.findById(requestTariffDTO.getMealPlanId()).orElseThrow(() -> new DataNotFoundException("MealPlan not found"));

        Tariff tariff = tariffMapper.requestToTariff(requestTariffDTO);
        tariff.setHotel(hotel);
        tariff.setMealPlan(mealPlan);

        List<Category> categories = categoryRepository.findAllById(requestTariffDTO.getCategoryIds());
        tariff.setCategories(categories);

        return tariffMapper.toResponseTariffDTO(tariffRepository.save(tariff));
    }

    @Override
    @Transactional
    public ResponseTariffDTO updateTariff(UUID id, UpdateTariffDTO updateTariffDTO) {
        Tariff tariff = tariffRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Tariff not found"));

        if (updateTariffDTO.getRefundable() != null) tariff.setRefundable(updateTariffDTO.getRefundable());
        if (updateTariffDTO.getCancelDaysBefore() != null)
            tariff.setCancelDaysBefore(updateTariffDTO.getCancelDaysBefore());
        if (updateTariffDTO.getMealPlanId() != null) {
            MealPlan mealPlan = mealPlanRepository.findById(updateTariffDTO.getMealPlanId()).orElseThrow(() -> new DataNotFoundException("MealPlan not found"));
            tariff.setMealPlan(mealPlan);
        }
        if (updateTariffDTO.getMealPrice() != null) tariff.setMealPrice(updateTariffDTO.getMealPrice());
        if (updateTariffDTO.getName() != null) tariff.setName(updateTariffDTO.getName());

        if (updateTariffDTO.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(updateTariffDTO.getCategoryIds());
            tariff.setCategories(categories);
        }

        return tariffMapper.toResponseTariffDTO(tariffRepository.save(tariff));
    }

    @Override
    @Transactional
    public void deleteTariff(UUID id) {
        tariffRepository.deleteById(id);
    }
}
