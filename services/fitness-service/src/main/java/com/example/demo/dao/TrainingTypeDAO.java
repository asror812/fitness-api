package com.example.demo.dao;


import com.example.demo.model.TrainingType;
import org.springframework.stereotype.Repository;
import java.util.*;


@Repository
public interface TrainingTypeDAO  extends GenericDAO<TrainingType>  {
    Optional<TrainingType> findByName(String name);
    List<TrainingType> getAll();
}
