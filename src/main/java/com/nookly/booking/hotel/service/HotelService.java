package com.nookly.booking.hotel.service;

import com.nookly.booking.exception.DataNotFoundException;
import com.nookly.booking.hotel.dto.CreateHotelDTO;
import com.nookly.booking.hotel.dto.HotelMapper;
import com.nookly.booking.hotel.dto.HotelResponseDTO;
import com.nookly.booking.hotel.dto.UpdateHotelDTO;
import com.nookly.booking.hotel.model.Hotel;
import com.nookly.booking.hotel.model.HotelStatus;
import com.nookly.booking.hotel.repository.IHotelRepository;
import com.nookly.booking.hotel.utils.GenerateHotelNumberService;
import com.nookly.booking.user.model.User;
import com.nookly.booking.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HotelService implements IHotelService {
    private final HotelMapper hotelMapper;
    private final IHotelRepository hotelRepository;
    private final UserService userService;
    private final GenerateHotelNumberService generateHotelNumberService;

    public HotelService(IHotelRepository hotelRepository,
                        UserService userService,
                        HotelMapper hotelMapper,
                        GenerateHotelNumberService generateHotelNumberService
    ) {
        this.hotelRepository = hotelRepository;
        this.userService = userService;
        this.hotelMapper = hotelMapper;
        this.generateHotelNumberService = generateHotelNumberService;
    }

    @Override
    public Optional<HotelResponseDTO> getHotelById(String hotelId) {
        return hotelRepository.findByHotelNumber(hotelId).map(hotelMapper::toHotelResponseDTO);
    }

    @Override
    public List<HotelResponseDTO> getAllHotels() {
        return hotelRepository.findAll().stream().map(hotelMapper::toHotelResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HotelResponseDTO createHotel(CreateHotelDTO createHotelDTO, UUID ownerId) {

        User user = userService.getUserById(ownerId).orElseThrow(() -> new DataNotFoundException("User not found"));

        Hotel hotel = hotelMapper.toHotelFromCreatHotelDTO(createHotelDTO);
        hotel.setOwner(user);
        hotel.setHotelNumber(generateHotelNumberService.generateUniqueHotelNumber());

        hotel = hotelRepository.save(hotel);
        return hotelMapper.toHotelResponseDTO(hotel);
    }

    @Override
    @Transactional
    public boolean deleteHotel(String hotelId) {
        if (!hotelRepository.existsByHotelNumber(hotelId)) return false;

        hotelRepository.deleteByHotelNumber(hotelId);
        return true;
    }

    @Override
    @Transactional
    public HotelResponseDTO updateHotel(String hotelId, UpdateHotelDTO updateHotelDTO) {
        Hotel hotel = hotelRepository.findByHotelNumber(hotelId)
                .orElseThrow(() -> new DataNotFoundException("Hotel not found"));

        if (updateHotelDTO.getName() != null) hotel.setName(updateHotelDTO.getName());
        if (updateHotelDTO.getStarRating() != null) hotel.setStarRating(updateHotelDTO.getStarRating());
        if (updateHotelDTO.getLocation() != null) {
            hotel.setStatus(HotelStatus.PENDING);
            hotel.setLocation(updateHotelDTO.getLocation());
        }
        if (updateHotelDTO.getLatitude() != null) hotel.setLatitude(updateHotelDTO.getLatitude());
        if (updateHotelDTO.getLongitude() != null) hotel.setLongitude(updateHotelDTO.getLongitude());
        if (updateHotelDTO.getDescription() != null) hotel.setDescription(updateHotelDTO.getDescription());
        if (updateHotelDTO.getStatus() != null) hotel.setStatus(updateHotelDTO.getStatus());
        if (updateHotelDTO.getEmail() != null) hotel.setEmail(updateHotelDTO.getEmail());
        if (updateHotelDTO.getPhone() != null) hotel.setPhone(updateHotelDTO.getPhone());

        Hotel updatedHotel = hotelRepository.save(hotel);

        return hotelMapper.toHotelResponseDTO(updatedHotel);

    }
}
