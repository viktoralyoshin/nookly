package com.nookly.booking.accommodation.amenity.service;

import com.nookly.booking.accommodation.amenity.dto.AmenityMapper;
import com.nookly.booking.accommodation.amenity.dto.RequestAmenityDTO;
import com.nookly.booking.accommodation.amenity.dto.ResponseAmenityOnlyDTO;
import com.nookly.booking.accommodation.amenity.model.Amenity;
import com.nookly.booking.accommodation.amenity.repository.IAmenityRepository;
import com.nookly.booking.exception.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmenityService implements IAmenityService {
    private final IAmenityRepository amenityRepository;
    private final AmenityMapper amenityMapper;

    public AmenityService(IAmenityRepository amenityRepository,
                          AmenityMapper amenityMapper
    ) {
        this.amenityRepository = amenityRepository;
        this.amenityMapper = amenityMapper;
    }

    @Override
    public ResponseAmenityOnlyDTO getAmenityById(Long id) {
        return amenityRepository.findById(id).map(amenityMapper::toResponseAmenityOnlyDTO).orElseThrow(() -> new DataNotFoundException("Amenity not found"));
    }

    @Override
    public List<ResponseAmenityOnlyDTO> getAllAmenities() {
        return amenityRepository.findAll().stream().map(amenityMapper::toResponseAmenityOnlyDTO).collect(Collectors.toList());
    }

    @Override
    public ResponseAmenityOnlyDTO createAmenity(RequestAmenityDTO amenityDTO) {
        return amenityMapper.toResponseAmenityOnlyDTO(amenityRepository.save(amenityMapper.toAmenity(amenityDTO)));
    }

    @Override
    public ResponseAmenityOnlyDTO updateAmenity(Long id, RequestAmenityDTO amenityDTO) {
        Amenity amenity = amenityRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Amenity not found"));

        if(amenityDTO.getName() != null){
            amenity.setName(amenityDTO.getName());
        }

        return amenityMapper.toResponseAmenityOnlyDTO(amenityRepository.save(amenity));
    }

    @Override
    public void deleteAmenity(Long id) {
        amenityRepository.deleteById(id);
    }
}
