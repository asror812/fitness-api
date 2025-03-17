package com.example.demo.dao;

import com.example.demo.model.Trainer;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class TrainerDAOImpl extends AbstractHibernateDAO<Trainer> implements TrainerDAO {
    private static final String HQL_FIND_TRAINER_BY_USERNAME = "FROM Trainer WHERE user.username = :username";
    private static final String HQL_GET_ALL = "FROM Trainer";
    private static final String NO_TRAINER_FOUND_WITH_USERNAME = "Trainer with username {} not found";
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerDAOImpl.class);

    public TrainerDAOImpl() {
        super(Trainer.class);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        try {
            TypedQuery<Trainer> query = entityManager.createQuery(HQL_FIND_TRAINER_BY_USERNAME, Trainer.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            LOGGER.warn(NO_TRAINER_FOUND_WITH_USERNAME, username);
            return Optional.empty();
        }
    }

    @Override
    public List<Trainer> getAll() {
        TypedQuery<Trainer> query = entityManager.createQuery(HQL_GET_ALL, Trainer.class);
        return query.getResultList();
    }
}
