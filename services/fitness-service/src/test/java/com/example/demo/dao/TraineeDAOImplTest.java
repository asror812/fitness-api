package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.model.Trainee;
import com.example.demo.model.User;

import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class TraineeDAOImplTest {

    @Mock
    private TypedQuery<Trainee> typedQuery;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TraineeDAOImpl traineeDAO;

    private Trainee trainee;

    @BeforeEach
    void initialize() {
        trainee = new Trainee(
                new Date(), "T", new User("asror", "r", "asror.r", "1234567890", true),
                Collections.emptyList(), Collections.emptySet());
    }

    @Test
    void delete() {
        traineeDAO.delete(trainee);
        verify(entityManager, times(1)).remove(trainee);
    }

     @Test
     void findByUsername_NoResultException() {
         String username = "qwerty";
         when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQuery);
         when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
         
         when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);

         Optional<Trainee> result = traineeDAO.findByUsername(username);
         assertTrue(result.isEmpty());
     }
    
    @Test
    void findByUsername_ShouldReturn_Trainee() {
        String username = "testUser";
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(trainee);

        Optional<Trainee> result = traineeDAO.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }
}
