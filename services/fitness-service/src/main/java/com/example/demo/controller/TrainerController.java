package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.request.TrainerUpdateRequestDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.TrainerUpdateResponseDTO;
import com.example.demo.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/fitness/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping("/profiles/{username}")
    public ResponseEntity<TrainerResponseDTO> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(trainerService.findByUsername(username));
    }

    @PutMapping
    public ResponseEntity<TrainerUpdateResponseDTO> update(@Valid @RequestBody TrainerUpdateRequestDTO requestDTO) {
        TrainerUpdateResponseDTO update = trainerService.update(requestDTO);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

}
