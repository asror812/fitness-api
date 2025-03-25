package com.example.service;

import org.springframework.stereotype.Service;
import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.dto.TrainerWorkloadResponseDTO;


@Service
public interface TrainerWorkloadService {

    public void processWorkload(TrainerWorkloadRequestDTO requestDTO);

    public TrainerWorkloadResponseDTO getTrainerWorkload(String username, int year, int month);
}
