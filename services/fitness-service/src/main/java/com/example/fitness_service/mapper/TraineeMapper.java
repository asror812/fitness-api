package com.example.fitness_service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.example.fitness_service.dto.request.TraineeSignUpRequestDTO;
import com.example.fitness_service.dto.request.TraineeUpdateRequestDTO;
import com.example.fitness_service.dto.response.TraineeResponseDTO;
import com.example.fitness_service.dto.response.TraineeUpdateResponseDTO;
import com.example.fitness_service.model.Trainee;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TraineeMapper
        implements
        GenericMapper<Trainee, TraineeSignUpRequestDTO, TraineeResponseDTO, TraineeUpdateRequestDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Trainee toEntity(TraineeSignUpRequestDTO createDto) {
        return modelMapper.map(createDto, Trainee.class);
    }

    @Override
    public void toEntity(TraineeUpdateRequestDTO updateDto, Trainee trainee) {
        trainee.setAddress(updateDto.getAddress());
        trainee.setDateOfBirth(updateDto.getDateOfBirth());
    }

    @Override
    public TraineeResponseDTO toResponseDTO(Trainee trainee) {
        return modelMapper.map(trainee, TraineeResponseDTO.class);
    }

    public TraineeUpdateResponseDTO toUpdateResponseDTO(Trainee trainee) {
        return modelMapper.map(trainee, TraineeUpdateResponseDTO.class);
    }

}