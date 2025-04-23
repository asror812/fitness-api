package com.example.demo.dao;

import com.example.demo.model.Training;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface TrainingDAO extends GenericDAO<Training> {

    List<Training> findTraineeTrainings(String username, Date from, Date to, String trainerName,
            String trainingType);

    List<Training> findTrainerTrainings(String username, Date from, Date to, String traineeName);

    List<Training> findTraineeTrainingsById(UUID id);
}
