package com.example.fitness_service.jms;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import jakarta.jms.ConnectionFactory;

@Configuration
@RequiredArgsConstructor
public class ActiveMQConfig {

    private final ActiveMQProperties properties;

    @Bean
    ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                properties.getUser(),
                properties.getPassword(),
                properties.getBrokerUrl());

        // Retry config
        factory.setTrustedPackages(List.of("com.example.fitness_service"));

        /*
         * RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
         * redeliveryPolicy.setMaximumRedeliveries(2);
         * redeliveryPolicy.setInitialRedeliveryDelay(2000);
         * redeliveryPolicy.setBackOffMultiplier(2);
         * redeliveryPolicy.setUseExponentialBackOff(true);
         * factory.setRedeliveryPolicy(redeliveryPolicy);
         */

        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setPubSubDomain(true);

        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory topicListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setPubSubDomain(true);
        return factory;
    }
}