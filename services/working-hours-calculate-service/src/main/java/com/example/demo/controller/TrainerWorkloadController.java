package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.TrainerWorkload;
import com.example.demo.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workload")
public class TrainerWorkloadController {

    private final TrainerWorkloadService workloadService;

    @GetMapping("/{username}")
    public TrainerWorkload getTrainerWorkload(@PathVariable String username) {
        return workloadService.getTrainerWorkload(username);
    }

}
