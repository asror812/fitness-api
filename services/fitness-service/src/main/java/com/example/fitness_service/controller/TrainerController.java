package com.example.fitness_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/fitness/trainers")
@RequiredArgsConstructor
public class TrainerController {

   /*@GetMapping("/profiles/{id}")
    public ResponseEntity<TrainerResponseDTO> getProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(trainerService.findById(id));
    }*/

    /* @PutMapping
    public ResponseEntity<TrainerUpdateResponseDTO> update(@Valid @RequestBody TrainerUpdateRequestDTO requestDTO) {
        TrainerUpdateResponseDTO update = trainerService.update(requestDTO);
        return new ResponseEntity<>(update, HttpStatus.OK);
    } */

}
