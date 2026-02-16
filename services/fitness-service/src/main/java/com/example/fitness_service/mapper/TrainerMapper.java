package com.example.fitness_service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.fitness_service.dao.TrainingTypeRepostiory;
import com.example.fitness_service.dto.request.TrainerSignUpRequestDTO;
import com.example.fitness_service.dto.request.TrainerUpdateRequestDTO;
import com.example.fitness_service.dto.response.TrainerResponseDTO;
import com.example.fitness_service.dto.response.TrainerUpdateResponseDTO;
import com.example.fitness_service.model.Trainer;
import com.example.fitness_service.model.TrainingType;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainerMapper
        implements
        GenericMapper<Trainer, TrainerSignUpRequestDTO, TrainerResponseDTO, TrainerUpdateRequestDTO> {

    private final ModelMapper modelMapper;
    private final TrainingTypeRepostiory trainingTypeRepostiory;

    @Override
    public Trainer toEntity(TrainerSignUpRequestDTO createDto) {
        return modelMapper.map(createDto, Trainer.class);
    }

    @Override
    public void toEntity(TrainerUpdateRequestDTO updateDto, Trainer trainer) {

        TrainingType specialization = trainingTypeRepostiory.findById(updateDto.getSpecialization())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No Training type found with id %s".formatted(updateDto.getSpecialization())));

        trainer.setSpecialization(specialization);
    }

    @Override
    public TrainerResponseDTO toResponseDTO(Trainer trainer) {
        return modelMapper.map(trainer, TrainerResponseDTO.class);
    }

    public TrainerUpdateResponseDTO toUpdateResponseDTO(Trainer trainer) {
        return modelMapper.map(trainer, TrainerUpdateResponseDTO.class);
    }

}
