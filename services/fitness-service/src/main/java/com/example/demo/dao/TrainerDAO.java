package com.example.demo.dao;

import com.example.demo.model.Trainer;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface TrainerDAO extends GenericDAO<Trainer> {

    Optional<Trainer> findByUsername(String username);

    List<Trainer> getAll();
}
