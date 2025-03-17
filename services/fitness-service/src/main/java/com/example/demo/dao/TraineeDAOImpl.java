package com.example.demo.dao;

import com.example.demo.model.Trainee;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class TraineeDAOImpl extends AbstractHibernateDAO<Trainee> implements TraineeDAO {
    private static final String HQL_FIND_TRAINEE_BY_USERNAME = "FROM Trainee WHERE user.username = :username"; 
    private static final String NO_TRAINEE_FOUND_WITH_USERNAME = "No Trainer found with username {}";

    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeDAOImpl.class);

    public TraineeDAOImpl() {
        super(Trainee.class);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        try {
            TypedQuery<Trainee> query = entityManager.createQuery(HQL_FIND_TRAINEE_BY_USERNAME, Trainee.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            LOGGER.warn(NO_TRAINEE_FOUND_WITH_USERNAME, username);
            return Optional.empty();
        }
    }

    @Override
    public void delete(Trainee trainee) {
        entityManager.remove(trainee);
    }

}
