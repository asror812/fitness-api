package com.example.demo.dao;

import com.example.demo.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO extends GenericDAO<Trainer> {

    Optional<Trainer> findByUsername(String username);

    List<Trainer> getAll();
}
