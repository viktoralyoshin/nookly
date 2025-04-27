package com.nookly.booking.pricing.tariff.service;

import com.nookly.booking.pricing.tariff.dto.RequestTariffDTO;
import com.nookly.booking.pricing.tariff.dto.ResponseTariffDTO;
import com.nookly.booking.pricing.tariff.dto.UpdateTariffDTO;

import java.util.List;
import java.util.UUID;

public interface ITariffService {
    ResponseTariffDTO getTariffById(UUID id);
    List<ResponseTariffDTO> getTariffs();
    ResponseTariffDTO createTariff(RequestTariffDTO requestTariffDTO);
    ResponseTariffDTO updateTariff(UUID id, UpdateTariffDTO updateTariffDTO);
    void deleteTariff(UUID id);
}
