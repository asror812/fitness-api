package com.example.auth_service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TraineeSignUpRequestDTO extends SignUpRequestDTO {

   private LocalDate dateOfBirth;

   private String address;

   public TraineeSignUpRequestDTO(String firstName, String lastName, LocalDate dateOfBirth, String address) {
      super(firstName, lastName);
      this.dateOfBirth = dateOfBirth;
      this.address = address;
   }

}
