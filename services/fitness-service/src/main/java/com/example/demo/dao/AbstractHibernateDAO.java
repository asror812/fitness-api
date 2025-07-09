package com.example.demo.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.demo.exception.DataAccessException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

@Repository
public abstract class AbstractHibernateDAO<T> {

    @PersistenceContext
    protected EntityManager entityManager;
    private final Class<T> clazz;

    private static final String FAILED_TO_GET_BY_ID = "Failed to get by id:  %s";
    private static final String FAILED_TO_CREATE = "Failed to create %s";
    private static final String FAILED_TO_UPDATE = "Failed to update %s";

    protected AbstractHibernateDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Optional<T> findById(UUID id) {
        try {
            return Optional.ofNullable(entityManager.find(clazz, id));
        } catch (PersistenceException ex) {
            throw new DataAccessException(FAILED_TO_GET_BY_ID.formatted(id), ex);
        }
    }

    public T create(T entity) {
        try {
            entityManager.persist(entity);
            return entity;
        } catch (PersistenceException ex) {
            throw new DataAccessException(FAILED_TO_CREATE.formatted(entity), ex);
        }
    }

    public void update(T entity) {
        try {
            entityManager.merge(entity);
        } catch (PersistenceException ex) {
            throw new DataAccessException(FAILED_TO_UPDATE.formatted(entity), ex);
        }
    }

}
