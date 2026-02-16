package com.example.workload_service.service;

import com.example.workload_service.dto.request.*;
import com.example.workload_service.model.TrainerWorkload;

public interface TrainerWorkloadService {

    void processWorkload(TrainerWorkloadRequestDTO requestDTO);

    TrainerWorkload getTrainerWorkload(String username);
}
