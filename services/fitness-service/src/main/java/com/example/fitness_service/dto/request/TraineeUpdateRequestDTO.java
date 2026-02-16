package com.example.fitness_service.dto.request;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraineeUpdateRequestDTO extends UserUpdateRequestDTO {

    private Date dateOfBirth;

    private String address;

    public TraineeUpdateRequestDTO(UUID id, String firstName, String lastName, Boolean active, Date dateOfBirth,
            String address) {
        super(id, firstName, lastName, active);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

}