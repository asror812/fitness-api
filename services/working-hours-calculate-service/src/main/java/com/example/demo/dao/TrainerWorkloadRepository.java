package com.example.demo.dao;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.TrainerWorkload;
import java.util.Optional;


@Repository
public interface TrainerWorkloadRepository extends MongoRepository<TrainerWorkload, String> {

    @Query("{ '_id': ?0 }")
    Optional<TrainerWorkload> findByUsername(String username);
}
