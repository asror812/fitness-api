package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dto.request.TrainerSignUpRequestDTO;
import com.example.demo.dto.request.TrainerUpdateRequestDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.TrainerUpdateResponseDTO;
import com.example.demo.model.Trainer;
import com.example.demo.model.TrainingType;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainerMapper
        implements
        GenericMapper<Trainer, TrainerSignUpRequestDTO, TrainerResponseDTO, TrainerUpdateRequestDTO> {

    private final ModelMapper modelMapper;
    private final TrainingTypeDAO dao;

    @Override
    public Trainer toEntity(TrainerSignUpRequestDTO createDto) {
        return modelMapper.map(createDto, Trainer.class);
    }

    @Override
    public void toEntity(TrainerUpdateRequestDTO updateDto, Trainer trainer) {
        trainer.getUser().setFirstName(updateDto.getFirstName());
        trainer.getUser().setLastName(updateDto.getLastName());
        trainer.getUser().setActive(updateDto.getActive());

        TrainingType specialization = dao.findById(updateDto.getSpecialization())
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
