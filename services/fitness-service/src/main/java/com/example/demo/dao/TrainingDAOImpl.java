package com.example.demo.dao;

import com.example.demo.model.Trainee;
import com.example.demo.model.Trainer;
import com.example.demo.model.Training;
import com.example.demo.model.TrainingType;
import com.example.demo.model.User;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class TrainingDAOImpl extends AbstractHibernateDAO<Training> implements TrainingDAO {
    private static final String FIND_TRAINING_BY_TRAINEE_ID = "FROM Training WHERE trainee.id = :id";
    private static final String DELETE_TRAINING_BY_ID = "DELETE FROM Training t WHERE t.id = :id";

    public TrainingDAOImpl() {
        super(Training.class);
    }

    public List<Training> findTraineeTrainingsById(UUID id) {
        TypedQuery<Training> query = entityManager.createQuery(FIND_TRAINING_BY_TRAINEE_ID, Training.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Training> findTraineeTrainings(String username, Date from,
            Date to, String trainerName,
            String trainingTypeName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> training = query.from(Training.class);

        // Joins
        Join<Training, Trainee> trainee = training.join("trainee");
        Join<Trainee, User> traineeUser = trainee.join("user");
        Join<Training, Trainer> trainer = training.join("trainer");
        Join<Trainer, User> trainerUser = trainer.join("user");
        Join<Training, TrainingType> type = training.join("trainingType");

        List<Predicate> predicates = new ArrayList<>();

        // Mandatory Filter: Trainee Username
        predicates.add(cb.equal(traineeUser.get("username"), username));

        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), from));
        }
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), to));
        }
        if (trainerName != null && !trainerName.trim().isEmpty()) {
            predicates.add(cb.equal(trainerUser.get("username"), trainerName));
        }
        if (trainingTypeName != null && !trainingTypeName.trim().isEmpty()) {
            predicates.add(cb.equal(type.get("trainingTypeName"), trainingTypeName));
        }

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Training> findTrainerTrainings(String username, Date from, Date to, String traineeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> training = query.from(Training.class);

        // Joins
        Join<Training, Trainee> trainee = training.join("trainee");
        Join<Trainee, User> traineeUser = trainee.join("user");
        Join<Training, Trainer> trainer = training.join("trainer");
        Join<Trainer, User> trainerUser = trainer.join("user");

        List<Predicate> predicates = new ArrayList<>();

        // Mandatory Filter: Trainer Username
        predicates.add(cb.equal(trainerUser.get("username"), username));

        // Predicates
        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), from));
        }
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), to));
        }
        if (traineeName != null && !traineeName.trim().isEmpty()) {
            predicates.add(cb.equal(traineeUser.get("username"), traineeName));
        }
        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void delete(UUID id) {
        jakarta.persistence.Query query = entityManager.createQuery(DELETE_TRAINING_BY_ID);;
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
