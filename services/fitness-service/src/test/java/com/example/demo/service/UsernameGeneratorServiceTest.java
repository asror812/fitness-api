package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;


@ExtendWith(MockitoExtension.class)
class UsernameGeneratorServiceTest {

    @InjectMocks
    private UsernameGeneratorService usernameGeneratorService;

    @Mock
    private UserDAO userDAO;
    
    @Test
    void generateUsername_ShouldReturn_Username() {
        when(userDAO.findByUsername("asror.r")).thenReturn(Optional.of(new User()));

        when(userDAO.findByUsername("asror.r1")).thenReturn(Optional.empty());

        String username2 = usernameGeneratorService.generateUsername("asror", "r");

        assertEquals("asror.r1", username2);
    }
}
