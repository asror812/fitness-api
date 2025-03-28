package com.example.service;

import org.springframework.stereotype.Service;
import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.model.TrainerWorkload;

@Service
public interface TrainerWorkloadService {

    public void processWorkload(TrainerWorkloadRequestDTO requestDTO);

    public TrainerWorkload getTrainerWorkload(String username);
}
