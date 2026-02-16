package com.example.auth_service.security;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    private List<String> allowedOrigins;

    private List<String> allowedMethods;

    public CorsProperties(List<String> allowedOrigins, List<String> allowedMethods) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
    }

}
