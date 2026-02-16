package com.example.gateway_server.dto.request;

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

   @NotBlank(message = "Username must not be empty")
   private String username;

   @NotBlank(message = "First name must not be empty")
   private String firstName;

   @NotBlank(message = "Last name must not be empty")
   private String lastName;

   @NotNull(message = "Active status must not be null")
   private boolean active;

}
