package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.TrainingTypeCreateDTO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.service.TrainingTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/3fitness/training-types")
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping
    public ResponseEntity<List<TrainingTypeResponseDTO>> getAll() {
        List<TrainingTypeResponseDTO> all = trainingTypeService.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrainingTypeResponseDTO> create(
            @Valid @RequestBody TrainingTypeCreateDTO createDTO) {
        return new ResponseEntity<>(trainingTypeService.create(createDTO), HttpStatus.CREATED);
    }

}