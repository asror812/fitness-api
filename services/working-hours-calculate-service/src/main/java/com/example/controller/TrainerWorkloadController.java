package com.example.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class TrainerWorkloadController {

    private final TrainerWorkloadService workloadService;

    @PostMapping
    public ResponseEntity<Void> increaseWorkload(@Valid @RequestBody TrainerWorkloadRequestDTO requestDTO) {
        workloadService.increaseWorkload(requestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> decreaseWorkload(@Valid @RequestBody TrainerWorkloadRequestDTO requestDTO) {
        workloadService.decreaseWorkload(requestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
