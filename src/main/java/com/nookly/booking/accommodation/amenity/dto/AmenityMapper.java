package com.nookly.booking.accommodation.amenity.dto;

import com.nookly.booking.accommodation.amenity.model.Amenity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    ResponseAmenityOnlyDTO toResponseAmenityOnlyDTO(Amenity amenity);

    Amenity toAmenity(RequestAmenityDTO requestAmenityDTO);
}
