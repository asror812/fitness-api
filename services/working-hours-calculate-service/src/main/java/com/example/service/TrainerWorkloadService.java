package com.example.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.model.TrainerWorkload;

@Service
public interface TrainerWorkloadService {

    public void processWorkload(TrainerWorkloadRequestDTO requestDTO);

    public List<TrainerWorkload> getTrainerWorkload(String username, int year, int month);
}
