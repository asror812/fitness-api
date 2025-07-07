package com.example.demo.jms;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ActiveMQConfigTest {

    private ActiveMQConfig config;


    @BeforeEach
    void setUp() {
        config.brokerUrl = "tcp://localhost:61616";
        config.username = "admin";
        config.password = "admin";
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
    void jmsListenerContainerFactory_ShouldReturnConfiguredFactory() {
        ConnectionFactory mockConnectionFactory = mock(ConnectionFactory.class);
        DefaultJmsListenerContainerFactory factory = config.jmsListenerContainerFactory(mockConnectionFactory);

        assertNotNull(factory);
    }
}
