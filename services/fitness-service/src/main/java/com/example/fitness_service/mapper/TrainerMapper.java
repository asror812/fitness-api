/* package com.example.fitness_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.fitness_service.dto.request.TrainerSignUpRequestDTO;
import com.example.fitness_service.dto.request.TrainerUpdateRequestDTO;
import com.example.fitness_service.dto.response.TrainerResponseDTO;
import com.example.fitness_service.dto.response.TrainerUpdateResponseDTO;
import com.example.fitness_service.model.Trainer;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    @Mapping(target = "datetimeCreated", ignore = true)
    @Mapping(target = "datetimeUpdated", ignore = true)
    @Mapping(target = "id", ignore = true)
    public Trainer toEntity(TrainerSignUpRequestDTO createDto);

    public void toEntity(TrainerUpdateRequestDTO updateDto, Trainer trainer);

    public TrainerResponseDTO toResponseDTO(Trainer trainer);

    public TrainerUpdateResponseDTO toUpdateResponseDTO(Trainer trainer);

}
 */