package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.model.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class TrainingTypeDAOImplTest {

    @InjectMocks
    private TrainingTypeDAOImpl trainingTypeDAO;

    @Mock
    private TypedQuery<TrainingType> typedQuery;

    @Mock
    private EntityManager entityManager;

    @Test
    void findByName_ShouldReturn() {
        String name = "qwerty";

        when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("type", name)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(new TrainingType());

        Optional<TrainingType> byName = trainingTypeDAO.findByName("qwerty");
        assertTrue(byName.isPresent());
    }

    @Test
    void findByName_ResourceNotFoundException() {
        String name = "qwerty";
        when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("type", name)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(null);

        Optional<TrainingType> byName = trainingTypeDAO.findByName("qwerty");
        assertTrue(byName.isEmpty());
    }

    @Test
    void getAll() {
        when(entityManager.createQuery(anyString(), eq(TrainingType.class))).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(List.of(new TrainingType(), new TrainingType()));

        assertEquals(2, trainingTypeDAO.getAll().size());
    }
}
