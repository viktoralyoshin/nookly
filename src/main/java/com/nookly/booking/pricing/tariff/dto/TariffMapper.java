package com.nookly.booking.pricing.tariff.dto;

import com.nookly.booking.pricing.tariff.model.Tariff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TariffMapper {
    ResponseTariffOnlyDTO toResponseTariffOnlyDTO(Tariff tariff);

    ResponseTariffDTO toResponseTariffDTO(Tariff tariff);

    @Mapping(target = "categories", ignore = true)
    Tariff updateToTariff(UpdateTariffDTO updateTariffDTO);

    @Mapping(target = "categories", ignore = true)
    Tariff requestToTariff(RequestTariffDTO requestTariffDTO);
}
