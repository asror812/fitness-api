package com.example.demo.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainersUpdateRequestDTO {

    @NotBlank(message = "Trainers list cannot be empty")
    private List<TrainerDTO> trainers;

    public static class TrainerDTO {
        @NotBlank(message = "Trainer username is required")
        private String username;

        public TrainerDTO(String username) {
            this.username = username;
        }
        
        public String getUsername() {
            return username;
        }
    }
}
