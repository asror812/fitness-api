package com.example.demo.dao;

import com.example.demo.model.TrainingType;
import java.util.*;

public interface TrainingTypeDAO extends GenericDAO<TrainingType> {
    Optional<TrainingType> findByName(String name);

    List<TrainingType> getAll();
}
