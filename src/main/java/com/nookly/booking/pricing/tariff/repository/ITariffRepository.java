package com.nookly.booking.pricing.tariff.repository;

import com.nookly.booking.pricing.tariff.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ITariffRepository extends JpaRepository<Tariff, UUID> {
}
