package com.example.demo.jms;

import java.util.Arrays;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}") String brokerUrl;

    @Value("${spring.activemq.user}") String username;

    @Value("${spring.activemq.user}") String password;

    private final ObjectMapper mapper;

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(username, password, brokerUrl);

        // Retry config
        factory.setTrustedPackages(Arrays.asList("com.example.demo.dto.request"));

        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        redeliveryPolicy.setInitialRedeliveryDelay(2000);
        redeliveryPolicy.setBackOffMultiplier(2);
        redeliveryPolicy.setUseExponentialBackOff(true);

        factory.setRedeliveryPolicy(redeliveryPolicy);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(mapper);
        return converter;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSessionTransacted(true);
        factory.setPubSubDomain(false);
        factory.setMessageConverter(jacksonJmsConverter());
        return factory;
    }

}