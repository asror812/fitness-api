package com.example.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.model.TrainerWorkload;
import com.example.model.WorkingMonth;

@Repository
public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, String> {

	@Query("""
			        SELECT DISTINCT wm FROM WorkingMonth wm
			        JOIN tw.years wy
			        JOIN wy.wrokload wm
			        WHERE tw.username = :username
			        AND wy.year = :year
			        AND wm.month = :month
			""")
	Optional<WorkingMonth> findByTrainerUsernameAndDate(String username, Integer year,
			Integer month);
}