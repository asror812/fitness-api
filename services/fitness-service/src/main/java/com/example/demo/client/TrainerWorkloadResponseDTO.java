package com.example.demo.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrainerWorkloadResponseDTO {
    private String username;
    
    private int year;
    
    private int month;

    private double totalHours;

}
