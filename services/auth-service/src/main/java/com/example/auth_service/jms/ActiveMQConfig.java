package com.example.auth_service.jms;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@RequiredArgsConstructor
public class ActiveMQConfig {

    private final ActiveMQProperties activeMQProperties;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                activeMQProperties.getUser(),
                activeMQProperties.getPassword(),
                activeMQProperties.getBrokerUrl());

        // Retry config
        factory.setTrustedPackages(Arrays.asList("com.example.auth_service.dto.event"));

        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        redeliveryPolicy.setInitialRedeliveryDelay(2000);
        redeliveryPolicy.setBackOffMultiplier(2);
        redeliveryPolicy.setUseExponentialBackOff(true);

        factory.setRedeliveryPolicy(redeliveryPolicy);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);

        return jmsTemplate;
    }
}