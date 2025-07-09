package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.model.Trainer;
import com.example.demo.model.TrainingType;
import com.example.demo.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class TrainerDAOImplTest {

    @Mock
    private TypedQuery<Trainer> typedQuery;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TrainerDAOImpl trainerDAO;

    private Trainer trainer;

    @BeforeEach
    void initialize() {
        trainer = new Trainer(new User(), new TrainingType(), Collections.emptyList(), Collections.emptySet());
    }

    @Test
    void findByUsername_Success() {
        String username = "qwerty";
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);

        when(typedQuery.getSingleResult()).thenReturn(trainer);

        Optional<Trainer> byUsername = trainerDAO.findByUsername(username);

        assertTrue(byUsername.isPresent());
    }

    @Test
    void findByUsername_NoResult() {
        String username = "qwerty";
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);

        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);

        assertEquals(Optional.empty(), trainerDAO.findByUsername(username));
    }

    @Test
    void getAll() {
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(trainer));

        List<Trainer> all = trainerDAO.getAll();

        assertNotNull(all);
        assertEquals(1, all.size());
    }
}
