package com.example.auth_service.service;

import com.example.auth_service.dto.request.ChangePasswordRequestDTO;
import com.example.auth_service.dto.request.SignInRequestDTO;
import com.example.auth_service.dto.request.SignUpRequestDTO;
import com.example.auth_service.dto.request.TraineeSignUpRequestDTO;
import com.example.auth_service.dto.request.TrainerSignUpRequestDTO;
import com.example.auth_service.dto.response.SignInResponseDTO;
import com.example.auth_service.dto.response.SignUpResponseDTO;
import com.example.auth_service.model.Role;
import com.example.auth_service.model.User;

import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    SignInResponseDTO login(SignInRequestDTO requestDTO);

    void changePassword(ChangePasswordRequestDTO requestDTO);

    User register(SignUpRequestDTO requestDTO, Role role);

    SignUpResponseDTO registerTrainee(TraineeSignUpRequestDTO requestDTO);

    SignUpResponseDTO registerTrainer(TrainerSignUpRequestDTO requestDTO);
}