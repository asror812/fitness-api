package com.example.workload_service.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.workload_service.model.TrainerWorkload;
import com.example.workload_service.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workloads")
public class TrainerWorkloadController {

    private final TrainerWorkloadService workloadService;

    @GetMapping("/{username}")
    public TrainerWorkload getTrainerWorkload(@PathVariable String username) {
        return workloadService.getTrainerWorkload(username);
    }

}
