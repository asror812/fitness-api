package com.example.gateway_server.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {

   @NotBlank(message = "First name must not be empty")
   @Size(max = 50, message = "First name must not exceed 50 characters")
   private String firstName;

   @NotBlank(message = "Last name must not be empty")
   @Size(max = 50, message = "Last name must not exceed 50 characters")
   private String lastName;
}
