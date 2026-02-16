package com.example.auth_service.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ErrorResponseDTO {
    private String message;
    private List<String> details;
    private String timestamp;
}
