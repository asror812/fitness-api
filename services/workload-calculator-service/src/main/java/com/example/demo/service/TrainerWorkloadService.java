package com.example.demo.service;

import com.example.demo.dto.request.*;
import com.example.demo.model.TrainerWorkload;

public interface TrainerWorkloadService {

    void processWorkload(TrainerWorkloadRequestDTO requestDTO);

    TrainerWorkload getTrainerWorkload(String username);
}
