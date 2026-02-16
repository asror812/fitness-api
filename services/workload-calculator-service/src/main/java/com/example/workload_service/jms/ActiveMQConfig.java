package com.example.workload_service.jms;

import java.util.Arrays;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ActiveMQConfig {

    private final ActiveMQProperties activeMQProperties;

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                activeMQProperties.getUser(),
                activeMQProperties.getPassword(),
                activeMQProperties.getBrokerUrl());

        // Retry config
        factory.setTrustedPackages(Arrays.asList("com.example.workload_service.dto.request"));

        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        redeliveryPolicy.setInitialRedeliveryDelay(2000);
        redeliveryPolicy.setBackOffMultiplier(2);
        redeliveryPolicy.setUseExponentialBackOff(true);

        factory.setRedeliveryPolicy(redeliveryPolicy);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSessionTransacted(true);
        factory.setPubSubDomain(false);
        return factory;
    }

}