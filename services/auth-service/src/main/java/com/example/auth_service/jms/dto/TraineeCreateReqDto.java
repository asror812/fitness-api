package com.example.auth_service.jms.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TraineeCreateReqDto {

    private LocalDate dateOfBirth;

    private String address;

    private UUID userId;

}
