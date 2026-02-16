package com.example.fitness_service.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDTO {

   @NotBlank(message = "Id must not be empty")
   private UUID id;

   @NotBlank(message = "First name must not be empty")
   private String firstName;

   @NotBlank(message = "Last name must not be empty")
   private String lastName;

   @NotNull(message = "Active status must not be null")
   private boolean active;

}
