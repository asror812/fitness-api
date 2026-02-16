package com.example.fitness_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDTO {

   @NotBlank(message = "Username must not be empty")
   private String username;

   @NotBlank(message = "Old password must not be empty")
   @Size(min = 8, message = "Old password must be at least 8 characters long")
   private String oldPassword;

   @NotBlank(message = "New password must not be empty")
   @Size(min = 8, message = "New password must be at least 8 characters long")
   private String newPassword;
}
