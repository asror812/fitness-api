package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.request.StatusRequestDTO;
import com.example.demo.service.TraineeService;
import com.example.demo.service.TrainerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @PatchMapping("/trainees/status")
    public ResponseEntity<Void> setTraineeStatus(@RequestBody @Valid StatusRequestDTO requestDTO) {
        traineeService.setStatus(requestDTO.getUsername() , requestDTO.isStatus());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/trainers/status")
    public ResponseEntity<Void> setTrainerStatus(@RequestBody @Valid StatusRequestDTO requestDTO) {
        trainerService.setStatus(requestDTO.getUsername(), requestDTO.isStatus());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
