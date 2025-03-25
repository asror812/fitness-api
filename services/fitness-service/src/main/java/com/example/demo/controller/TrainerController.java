package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.client.TrainerWorkloadResponseDTO;
import com.example.demo.dto.request.TrainerUpdateRequestDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.TrainerUpdateResponseDTO;
import com.example.demo.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/profiles/{username}")
    public ResponseEntity<TrainerResponseDTO> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(trainerService.findByUsername(username).orElse(null));
    }

    @PutMapping
    public ResponseEntity<TrainerUpdateResponseDTO> update(@Valid @RequestBody TrainerUpdateRequestDTO requestDTO) {
        TrainerUpdateResponseDTO update = trainerService.update(requestDTO);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @GetMapping("/{username}/{year}/{month}")
    public ResponseEntity<TrainerWorkloadResponseDTO> getTrainerMonthlyWorkload(@PathVariable String username, @PathVariable int year, @PathVariable int month) {
        return new ResponseEntity<>(trainerService.getTrainerMonthlyWorkloadSummary(username, year, month), HttpStatus.OK);
    }
}
