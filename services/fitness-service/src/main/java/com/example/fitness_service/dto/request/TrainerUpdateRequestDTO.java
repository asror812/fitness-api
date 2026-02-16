package com.example.fitness_service.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainerUpdateRequestDTO extends UserUpdateRequestDTO {

    @NotNull(message = "Specialization must not be null")
    private UUID specialization;

    public TrainerUpdateRequestDTO(UUID id, String firstName, String lastName,
            boolean active, UUID specialization) {
        super(id, firstName, lastName, active);
        this.specialization = specialization;
    }

}