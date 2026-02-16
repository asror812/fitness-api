package com.example.fitness_service.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fitness_service.model.Trainee;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, UUID> {

}
