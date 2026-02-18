package com.example.auth_service.dto.response;

import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {
    private String message;
    private List<String> details;
    private String timestamp;
}
