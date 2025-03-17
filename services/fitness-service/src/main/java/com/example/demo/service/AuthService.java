package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.dto.request.ChangePasswordRequestDTO;
import com.example.demo.dto.request.SignInRequestDTO;
import com.example.demo.dto.request.SignUpRequestDTO;
import com.example.demo.dto.response.SignInResponseDTO;
import com.example.demo.dto.response.SignUpResponseDTO;

@Service
public interface AuthService {
    SignInResponseDTO login(SignInRequestDTO requestDTO);

    void changePassword(ChangePasswordRequestDTO requestDTO);

    SignUpResponseDTO register(SignUpRequestDTO requestDTO);
}