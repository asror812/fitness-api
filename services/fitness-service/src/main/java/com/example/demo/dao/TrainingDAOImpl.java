package com.example.demo.dao;

import com.example.demo.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class TrainingDAOImpl extends AbstractHibernateDAO<Training> implements TrainingDAO {
    private static final String FIND_TRAINING_BY_TRAINEE_ID = "FROM Training WHERE trainee.id = :id";

    public TrainingDAOImpl() {
        super(Training.class);
    }

    public List<Training> findTraineeTrainingsById(UUID id) {
        TypedQuery<Training> query = entityManager.createQuery(FIND_TRAINING_BY_TRAINEE_ID, Training.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Training> findTraineeTrainings(String username, Date from, Date to, String trainerName, String trainingTypeName) {
        return findTrainings(
                username, "trainee",
                from, to,
                trainerName, "trainer",
                trainingTypeName
        );
    }

    @Override
    public List<Training> findTrainerTrainings(String username, Date from, Date to, String traineeName) {
        return findTrainings(
                username, "trainer",
                from, to,
                traineeName, "trainee",
                null
        );
    }

    private List<Training> findTrainings(
            String username,
            String usernameRole,
            Date from,
            Date to,
            String otherName,
            String otherRole,
            String trainingTypeName
    ) {
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

        // Mandatory Filter
        if ("trainee".equals(usernameRole)) {
            predicates.add(cb.equal(traineeUser.get("username"), username));
        } else if ("trainer".equals(usernameRole)) {
            predicates.add(cb.equal(trainerUser.get("username"), username));
        }

        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), from));
        }
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), to));
        }

        if (otherName != null && !otherName.trim().isEmpty()) {
            if ("trainer".equals(otherRole)) {
                predicates.add(cb.equal(trainerUser.get("username"), otherName));
            } else if ("trainee".equals(otherRole)) {
                predicates.add(cb.equal(traineeUser.get("username"), otherName));
            }
        }

        if (trainingTypeName != null && !trainingTypeName.trim().isEmpty()) {
            predicates.add(cb.equal(type.get("trainingTypeName"), trainingTypeName));
        }

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }
}
