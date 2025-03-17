package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.example.demo.dto.request.TrainingCreateRequestDTO;
import com.example.demo.dto.request.TrainingUpdateRequestDTO;
import com.example.demo.dto.response.TraineeTrainingResponseDTO;
import com.example.demo.dto.response.TrainerTrainingResponseDTO;
import com.example.demo.dto.response.TrainingResponseDTO;
import com.example.demo.model.Training;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainingMapper
        implements
        GenericMapper<Training, TrainingCreateRequestDTO, TrainingResponseDTO, TrainingUpdateRequestDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Training toEntity(TrainingCreateRequestDTO createDto) {
        return modelMapper.map(createDto, Training.class);
    }

    @Override
    public TrainingResponseDTO toResponseDTO(Training training) {
        return modelMapper.map(training, TrainingResponseDTO.class);
    }

    @Override
    public void toEntity(TrainingUpdateRequestDTO updateDto, Training training) {
        modelMapper.map(training, updateDto);
    }

    public TrainerTrainingResponseDTO toTrainerTrainingResponseDTO(Training training) {
        TrainerTrainingResponseDTO map = modelMapper.map(training, TrainerTrainingResponseDTO.class);
        map.setTraineeName(training.getTrainer().getUser().getUsername());
        return map;
    }

    public TraineeTrainingResponseDTO toTraineeTrainingResponseDTO(Training training) {
        TraineeTrainingResponseDTO map = modelMapper.map(training, TraineeTrainingResponseDTO.class);
        map.setTrainerName(training.getTrainer().getUser().getUsername());
        return map;
    }

}