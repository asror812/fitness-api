package com.example.fitness_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.fitness_service.dto.response.TrainerResponseDTO;
import com.example.fitness_service.service.TrainerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/fitness/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/profiles/{id}")
    public ResponseEntity<TrainerResponseDTO> getProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(trainerService.findById(id));
    }

    /* @PutMapping
    public ResponseEntity<TrainerUpdateResponseDTO> update(@Valid @RequestBody TrainerUpdateRequestDTO requestDTO) {
        TrainerUpdateResponseDTO update = trainerService.update(requestDTO);
        return new ResponseEntity<>(update, HttpStatus.OK);
    } */

}
