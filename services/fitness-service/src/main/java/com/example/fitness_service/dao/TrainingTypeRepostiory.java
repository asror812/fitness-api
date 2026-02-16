package com.example.fitness_service.dao;

import com.example.fitness_service.model.TrainingType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface TrainingTypeRepostiory extends JpaRepository<TrainingType, UUID> {

    Optional<TrainingType> findByName(String name);

}
