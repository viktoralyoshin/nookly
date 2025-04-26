package com.nookly.booking.auth.dto;

import com.nookly.booking.user.dto.UserResponseDTO;

public class AuthResponseDTO {
    UserResponseDTO user;
    String accessToken;

    public AuthResponseDTO(UserResponseDTO user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
