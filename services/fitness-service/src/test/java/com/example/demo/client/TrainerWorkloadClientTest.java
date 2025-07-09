package com.example.demo.client;


import com.example.demo.jms.TrainerWorkloadJmsProducer;
import com.example.demo.security.JwtService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TrainerWorkloadJmsProducer consumer;

    @Mock
    private JmsTemplate jmsTemplate;

}
