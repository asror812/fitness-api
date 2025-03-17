package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.example.demo.dto.request.TraineeSignUpRequestDTO;
import com.example.demo.dto.request.TraineeUpdateRequestDTO;
import com.example.demo.dto.response.TraineeResponseDTO;
import com.example.demo.dto.response.TraineeUpdateResponseDTO;
import com.example.demo.model.Trainee;
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
        trainee.getUser().setFirstName(updateDto.getFirstName());
        trainee.getUser().setLastName(updateDto.getLastName());
        trainee.getUser().setActive(updateDto.getActive());
    }

    @Override
    public TraineeResponseDTO toResponseDTO(Trainee trainee) {
        return modelMapper.map(trainee, TraineeResponseDTO.class);
    }

    public TraineeUpdateResponseDTO toUpdateResponseDTO(Trainee trainee) {
        return modelMapper.map(trainee, TraineeUpdateResponseDTO.class);
    }

}