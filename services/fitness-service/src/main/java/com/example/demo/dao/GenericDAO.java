package com.example.demo.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public interface GenericDAO<E> {
    E create(E entity);

    void update(E entity);

    Optional<E> findById(UUID id);
}
