package com.example.auth_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TrainerUpdateRequestDTO extends UserUpdateRequestDTO {

    @NotNull(message = "Specialization must not be null")
    private UUID specialization;

    public TrainerUpdateRequestDTO(String username, String firstName, String lastName,
            boolean active, UUID specialization) {
        super(username, firstName, lastName, active);
        this.specialization = specialization;
    }

}