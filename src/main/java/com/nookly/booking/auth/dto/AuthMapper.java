package com.nookly.booking.auth.dto;

import com.nookly.booking.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    User toUser(AuthRegisterDTO authRegisterDTO);
}
