package com.example.fitness_service.dao;

import com.example.fitness_service.model.Trainer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID>{

}
