package com.example.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.model.TrainerWorkload;
import com.example.service.TrainerWorkloadService;  
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequiredArgsConstructor
@RequestMapping("/workload")
public class TrainerWorkloadController {

    private final TrainerWorkloadService workloadService;

    @PostMapping
    public ResponseEntity<Void> processWorkload(@RequestBody TrainerWorkloadRequestDTO requestDTO) {
        workloadService.processWorkload(requestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public TrainerWorkload getMonthlyWorkload(@PathVariable String username) {
        return workloadService.getTrainerWorkload(username);   
    }
    

}
