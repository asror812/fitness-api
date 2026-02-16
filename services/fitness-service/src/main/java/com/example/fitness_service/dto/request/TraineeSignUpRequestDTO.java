package com.example.fitness_service.dto.request;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraineeSignUpRequestDTO extends SignUpRequestDTO {

   private Date dateOfBirth;

   private String address;

   public TraineeSignUpRequestDTO(String firstName, String lastName, Date dateOfBirth, String address) {
      super(firstName, lastName);
      this.dateOfBirth = dateOfBirth;
      this.address = address;
   }

}
