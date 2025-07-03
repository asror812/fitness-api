package com.example.demo.dao;

import com.example.demo.model.Trainee;
import java.util.Optional;

public interface TraineeDAO extends GenericDAO<Trainee> {
    void delete(Trainee trainee);

    Optional<Trainee> findByUsername(String username);
}
