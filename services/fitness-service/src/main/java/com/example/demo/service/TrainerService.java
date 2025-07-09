package com.example.demo.service;

import com.example.demo.dto.request.TrainerSignUpRequestDTO;
import com.example.demo.dto.request.TrainerUpdateRequestDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.TrainerUpdateResponseDTO;

public interface TrainerService extends GenericService<TrainerUpdateRequestDTO, TrainerUpdateResponseDTO> {

    TrainerResponseDTO findByUsername(String username);

    void setStatus(String username, boolean status);

    SignUpResponseDTO register(TrainerSignUpRequestDTO requestDTO);
}
