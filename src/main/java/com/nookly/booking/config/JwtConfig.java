package com.nookly.booking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
    private Long accessExpiration;
    private Long refreshExpiration;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getAccessExpiration() {
        return accessExpiration;
    }

    public void setAccessExpiration(Long accessExpiration) {
        this.accessExpiration = accessExpiration;
    }

    public Long getRefreshExpiration() {
        return refreshExpiration;
    }

    public void setRefreshExpiration(Long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }
}
