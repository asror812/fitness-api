package com.example.demo.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
