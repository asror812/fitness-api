package com.example.gateway_server.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TrainerSignUpRequestDTO extends SignUpRequestDTO {

    @NotNull(message = "Specialization must not be null")
    private UUID specialization;

    public TrainerSignUpRequestDTO(String firstName, String lastName, UUID specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
    }
}
