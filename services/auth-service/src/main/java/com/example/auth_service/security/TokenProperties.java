package com.example.auth_service.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class TokenProperties {

    private Long duration;

    private String key;
}
