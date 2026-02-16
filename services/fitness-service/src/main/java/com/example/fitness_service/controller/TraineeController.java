package com.example.fitness_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.fitness_service.dto.response.TraineeResponseDTO;
import com.example.fitness_service.dto.response.TrainerResponseDTO;
import com.example.fitness_service.service.TraineeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fitness/trainees")
public class TraineeController {
    private final TraineeService traineeService;

    @GetMapping("/profiles/{id}")
    public ResponseEntity<TraineeResponseDTO> getProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(traineeService.findById(id));
    }

/*     @PutMapping
    public ResponseEntity<TraineeUpdateResponseDTO> update(@Valid @RequestBody TraineeUpdateRequestDTO requestDTO) {
        TraineeUpdateResponseDTO update = traineeService.update(requestDTO);
        return new ResponseEntity<>(update, HttpStatus.OK);
    } */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        traineeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/not-assigned-trainers")
    public ResponseEntity<List<TrainerResponseDTO>> getNotAssignedTrainers(@PathVariable UUID id) {
        return new ResponseEntity<>(traineeService.getNotAssignedTrainers(id), HttpStatus.OK);
    }

/*     // TODO: CHECK
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerResponseDTO>> updateTraineeAssignedTrainers(@PathVariable String username,
            @RequestBody TraineeTrainersUpdateRequestDTO requestDTO) {
        return new ResponseEntity<>(traineeService.updateTraineeTrainers(username, requestDTO), HttpStatus.OK);
    } */
}
