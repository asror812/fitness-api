package com.example.demo.dto.request;

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

    public TrainerUpdateRequestDTO(String username, String firstName, String lastName,
            Boolean active, UUID specialization) {
        super(username, firstName, lastName, active);
        this.specialization = specialization;
    }

}