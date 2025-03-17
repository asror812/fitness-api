package com.example.demo.dao;

import com.example.demo.model.TrainingType;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class TrainingTypeDAOImpl extends AbstractHibernateDAO<TrainingType> implements TrainingTypeDAO {

    private static final String HQL_FIND_TRAINING_TYPE_BY_NAME = "FROM TrainingType WHERE trainingTypeName = :type";
    private static final String HQL_GET_ALL = "FROM TrainingType ";
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingTypeDAOImpl.class);
    private static final String NO_TRAINING_FOUND_WITH_NAME = "No Training type found with name {}";

    public TrainingTypeDAOImpl() {
        super(TrainingType.class);
    }

    @Override
    public Optional<TrainingType> findByName(String name) {
        try {
            TypedQuery<TrainingType> query = entityManager.createQuery(HQL_FIND_TRAINING_TYPE_BY_NAME,TrainingType.class);
            query.setParameter("type", name);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            LOGGER.warn(NO_TRAINING_FOUND_WITH_NAME, name);
            return Optional.empty();
        }
    }


    @Override
    public List<TrainingType> getAll() {
        TypedQuery<TrainingType> query = entityManager.createQuery(HQL_GET_ALL, TrainingType.class);
        return query.getResultList();
    }

}
