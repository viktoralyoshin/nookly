package com.nookly.booking.accommodation.amenity.service;

import com.nookly.booking.accommodation.amenity.dto.RequestAmenityDTO;
import com.nookly.booking.accommodation.amenity.dto.ResponseAmenityOnlyDTO;

import java.util.List;

public interface IAmenityService {
    ResponseAmenityOnlyDTO getAmenityById(Long id);
    List<ResponseAmenityOnlyDTO> getAllAmenities();
    ResponseAmenityOnlyDTO createAmenity(RequestAmenityDTO amenityDTO);
    ResponseAmenityOnlyDTO updateAmenity(Long id, RequestAmenityDTO amenityDTO);
    void deleteAmenity(Long id);
}
