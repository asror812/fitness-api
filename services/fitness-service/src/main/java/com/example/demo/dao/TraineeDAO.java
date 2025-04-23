package com.example.demo.dao;

import com.example.demo.model.Trainee;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TraineeDAO extends GenericDAO<Trainee> {
    void delete(Trainee trainee);

    Optional<Trainee> findByUsername(String username);
}
