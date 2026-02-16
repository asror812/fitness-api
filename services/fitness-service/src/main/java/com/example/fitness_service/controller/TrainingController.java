/* package com.example.fitness_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.fitness_service.dto.request.TrainingCreateRequestDTO;
import com.example.fitness_service.dto.response.TrainingResponseDTO;
import com.example.fitness_service.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fitness/trainings")
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    public ResponseEntity<Void> addTraining(@Valid @RequestBody TrainingCreateRequestDTO requestDTO) {
        trainingService.create(requestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/trainer/{username}")
    public ResponseEntity<List<TrainingResponseDTO>> getTrainerTrainers(@PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(required = false) String traineeName) {

        List<TrainingResponseDTO> trainerTrainings = trainingService.getTrainerTrainings(username, from, to,
                traineeName);
        return ResponseEntity.ok(trainerTrainings);
    }

    @GetMapping("/trainee/{username}")
    public ResponseEntity<List<TrainingResponseDTO>> getTraineeTrainings(@PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingTypeName) {

        List<TrainingResponseDTO> traineeTrainings = trainingService.getTraineeTrainings(username, from, to,
                trainerName, trainingTypeName);

        return ResponseEntity.ok(traineeTrainings);
    }

}
 */