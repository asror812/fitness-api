package com.example.demo.dto.request;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraineeUpdateRequestDTO extends UserUpdateRequestDTO {
    
    private Date dateOfBirth;
    
    private String address;

    public TraineeUpdateRequestDTO(String username, String firstName, String lastName, Boolean active, Date dateOfBirth,
            String address) {
        super(username, firstName, lastName, active);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    
}