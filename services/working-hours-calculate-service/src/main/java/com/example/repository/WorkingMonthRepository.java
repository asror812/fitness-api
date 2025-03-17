package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.WorkingMonth;

@Repository
public interface WorkingMonthRepository extends JpaRepository<WorkingMonth, Integer> {

}
