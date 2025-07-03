package com.example.demo.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jms.support.converter.MessageConverter;

import static org.junit.jupiter.api.Assertions.*;

class ActiveMQConfigTest {

    private ActiveMQConfig config;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        config.brokerUrl = "tcp://localhost:61616";
        config.username = "admin";
        config.password = "admin";
        mapper = new ObjectMapper();
        config.mapper = mapper;
    }

    @Test
    void connectionFactory_ShouldCreateActiveMQFactoryWithRetryPolicy() {
        ConnectionFactory connectionFactory = config.connectionFactory();

        assertNotNull(connectionFactory);
        assertTrue(connectionFactory instanceof ActiveMQConnectionFactory);

        ActiveMQConnectionFactory activeFactory = (ActiveMQConnectionFactory) connectionFactory;
        assertEquals("tcp://localhost:61616", activeFactory.getBrokerURL());
        assertEquals("admin", activeFactory.getUserName());
        assertNotNull(activeFactory.getRedeliveryPolicy());

        RedeliveryPolicy policy = activeFactory.getRedeliveryPolicy();
        assertEquals(3, policy.getMaximumRedeliveries());
        assertEquals(2000, policy.getInitialRedeliveryDelay());
        assertTrue(policy.isUseExponentialBackOff());
    }

    @Test
    void jacksonJmsConverter_ShouldReturnConfiguredConverter() {
        MessageConverter converter = config.jacksonJmsConverter();
        assertNotNull(converter);
    }
}