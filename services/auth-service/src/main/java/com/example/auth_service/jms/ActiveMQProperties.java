package com.example.auth_service.jms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "spring.activemq")
@Getter
@Setter
public class ActiveMQProperties {

    private String user;

    private String password;

    private String brokerUrl;
}
