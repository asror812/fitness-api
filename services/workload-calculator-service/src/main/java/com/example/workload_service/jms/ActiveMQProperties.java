package com.example.workload_service.jms;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "spring.activemq")
@Getter
@Setter
public class ActiveMQProperties {
    private final String brokerUrl;

    private final String user;

    private final String password;

    public ActiveMQProperties(String brokerUrl, String user, String password) {
        this.brokerUrl = brokerUrl;
        this.user = user;
        this.password = password;
    }
}
