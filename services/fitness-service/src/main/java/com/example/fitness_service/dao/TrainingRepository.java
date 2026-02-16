package com.example.fitness_service.dao;

import com.example.fitness_service.model.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<Training, UUID> {

}
