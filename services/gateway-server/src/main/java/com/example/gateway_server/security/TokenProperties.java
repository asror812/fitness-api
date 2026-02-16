package com.example.gateway_server.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class TokenProperties {

    @Value("${token.duration}")
    private Long duration;

    @Value("${token.security-key}")
    private String token;

    public TokenProperties(Long duration, String token) {
        this.duration = duration;
        this.token = token;
    }
}
