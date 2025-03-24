package com.example.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.model.TrainerWorkload;
import com.example.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    @PostMapping("/addOrRemoveWorkload")
    public ResponseEntity<Void> processWorkload(@Valid @RequestBody TrainerWorkloadRequestDTO requestDTO) {
        workloadService.processWorkload(requestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{username}/{year}/{month}")
    public List<TrainerWorkload> getMonthlyWorkload(@PathVariable String username, @PathVariable int year, @PathVariable int month) {
        return workloadService.getTrainerWorkload(username, year, month);   
    }
    

}
