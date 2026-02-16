package com.example.fitness_service.dto.request;

import java.util.List;
import java.util.UUID;

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
        @NotBlank(message = "Trainer id is required")
        private UUID id;

        public TrainerDTO(@NotBlank(message = "Trainer id is required") UUID id) {
            this.id = id;
        }

        public TrainerDTO() {
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

    }
}
