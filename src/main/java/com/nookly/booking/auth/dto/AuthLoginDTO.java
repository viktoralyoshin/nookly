package com.nookly.booking.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthLoginDTO {
    @NotBlank
    private String phone;
    @NotBlank
    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
