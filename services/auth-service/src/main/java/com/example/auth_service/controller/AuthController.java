package com.example.auth_service.controller;

import com.example.auth_service.dto.request.ChangePasswordRequestDTO;
import com.example.auth_service.dto.request.SignInRequestDTO;
import com.example.auth_service.dto.request.TraineeSignUpRequestDTO;
import com.example.auth_service.dto.request.TrainerSignUpRequestDTO;
import com.example.auth_service.dto.response.SignInResponseDTO;
import com.example.auth_service.dto.response.SignUpResponseDTO;
import com.example.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/trainees/signup")
    public ResponseEntity<SignUpResponseDTO> signUpTrainee(@Valid @RequestBody TraineeSignUpRequestDTO requestDTO) {
        SignUpResponseDTO register = authService.registerTrainee(requestDTO);
        return new ResponseEntity<>(register, HttpStatus.CREATED);
    }

    @PostMapping("/trainers/signup")
    public ResponseEntity<SignUpResponseDTO> signUpTrainer(@Valid @RequestBody TrainerSignUpRequestDTO requestDTO) {
        SignUpResponseDTO register = authService.registerTrainer(requestDTO);
        return new ResponseEntity<>(register, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDTO> signIn(@Valid @RequestBody SignInRequestDTO requestDTO) {
        return ResponseEntity.ok(authService.login(requestDTO));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequestDTO requestDTO) {
        authService.changePassword(requestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
