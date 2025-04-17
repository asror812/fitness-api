package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.*;
import com.example.demo.model.TrainerWorkload;

@Service
public interface TrainerWorkloadService {

    public void processWorkload(TrainerWorkloadRequestDTO requestDTO);

    public TrainerWorkload getTrainerWorkload(String username);
}
