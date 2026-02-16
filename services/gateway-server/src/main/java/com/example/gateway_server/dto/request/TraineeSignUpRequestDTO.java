package com.example.gateway_server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
