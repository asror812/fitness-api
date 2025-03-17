package com.example.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.model.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class TrainingDAOImplTest {

    @Mock
    private TypedQuery<Training> typedQuery;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TrainingDAOImpl trainingDAO;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Training> criteriaQuery;

    @Mock
    private Root<Training> trainingRoot;

    @Mock
    private Join<Object, Object> traineeJoin;

    @Mock
    private Join<Object, Object> traineeUserJoin;

    @Mock
    private Join<Object, Object> trainerJoin;

    @Mock
    private Join<Object, Object> trainerUserJoin;

    @Mock
    private Join<Object, Object> typeJoin;

    @Mock
    private TypedQuery<Training> trainingQuery;

    @Test
    void findTraineeTrainings() {
        String username = "asror.r";
        Date from = new Date();
        Date to = new Date();
        String trainerName = "abror.r";
        String trainingTypeName = "Swimming";

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(trainingRoot);

        when(trainingRoot.join("trainee")).thenReturn(traineeJoin);
        when(traineeJoin.join("user")).thenReturn(traineeUserJoin);
        when(trainingRoot.join("trainer")).thenReturn(trainerJoin);
        when(trainerJoin.join("user")).thenReturn(trainerUserJoin);
        when(trainingRoot.join("trainingType")).thenReturn(typeJoin);
    

        when(entityManager.createQuery(criteriaQuery)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(Collections.emptyList());

        when(entityManager.createQuery(criteriaQuery).getResultList()).thenReturn(List.of(new Training()));

        List<Training> traineeTrainings = trainingDAO.findTraineeTrainings(username, from, to, trainerName,
                trainingTypeName);

        assertEquals(1, traineeTrainings.size());
        assertNotNull(traineeTrainings);

    }

    @Test
    void findTraineeTrainingsById() {
        UUID id = UUID.randomUUID();
        when(entityManager.createQuery(anyString(), eq(Training.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("id", id)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(new Training(), new Training()));

        assertEquals(2, trainingDAO.findTraineeTrainingsById(id).size());
    }

    @Test
    void findTrainerTrainings() {
        String username = "asror.r";
        Date from = new Date();
        Date to = new Date();
        String traineeName = "abror.r";

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Training.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Training.class)).thenReturn(trainingRoot);

        when(trainingRoot.join("trainee")).thenReturn(traineeJoin);
        when(traineeJoin.join("user")).thenReturn(traineeUserJoin);
        when(trainingRoot.join("trainer")).thenReturn(trainerJoin);
        when(trainerJoin.join("user")).thenReturn(trainerUserJoin);
  

        when(entityManager.createQuery(criteriaQuery)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(Collections.emptyList());

        when(entityManager.createQuery(criteriaQuery).getResultList()).thenReturn(List.of(new Training()));

        List<Training> trainerTrainings = trainingDAO.findTrainerTrainings(username, from, to, traineeName);

        assertEquals(1, trainerTrainings.size());
        assertNotNull(trainerTrainings);
    }
}
