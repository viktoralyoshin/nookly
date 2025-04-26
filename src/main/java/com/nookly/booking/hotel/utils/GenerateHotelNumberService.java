package com.nookly.booking.hotel.utils;

import com.nookly.booking.hotel.repository.IHotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class GenerateHotelNumberService {
    private final IHotelRepository hotelRepository;
    public GenerateHotelNumberService(IHotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    private String generateHotelNumber() {
        Random random = new Random();
        int number = random.nextInt(100_000_000);
        return String.format("%08d", number);
    }

    @Transactional(readOnly = true)
    public String generateUniqueHotelNumber() {
        String hotelNumber;
        do {
            hotelNumber = generateHotelNumber();
        } while (hotelRepository.existsByHotelNumber(hotelNumber));

        return hotelNumber;
    }
}
