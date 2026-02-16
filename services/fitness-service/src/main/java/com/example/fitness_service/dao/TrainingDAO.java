package com.example.fitness_service.dao;

import com.example.fitness_service.model.Training;
import java.util.*;

public interface TrainingDAO extends GenericDAO<Training> {

    List<Training> findTraineeTrainings(String username, Date from, Date to, String trainerName,
            String trainingType);

    List<Training> findTrainerTrainings(String username, Date from, Date to, String traineeName);

    List<Training> findTraineeTrainingsById(UUID id);
}
