package com.example.demo.controller;

import static org.mockito.Mockito.when;
import java.util.Date;
import java.util.UUID;

import com.example.demo.utils.BruteForceProtectorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.demo.dto.request.TraineeUpdateRequestDTO;
import com.example.demo.dto.request.TrainerUpdateRequestDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.JwtService;
import com.example.demo.service.TrainerService;

@WebMvcTest(TrainerController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainerService trainerService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockitoBean
    private BruteForceProtectorService bruteForceProtectorService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String endpoint = "/api/v1/fitness/trainers";

    @Test
    void getProfile_ShouldReturn_200() throws Exception {
        TrainerResponseDTO responseDTO = new TrainerResponseDTO(
                new UserResponseDTO("asror", "r", true),
                new TrainingTypeResponseDTO(UUID.randomUUID(), "Swimming"));

        when(trainerService.findByUsername("asror.r")).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders
                .get(endpoint + "/profiles/{username}", "asror.r")
                .header("Authorization", "Bearer " + jwtService.generateToken("a.a"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.firstName").value("asror"));
    }

    @Test
    void getProfile_ShouldReturn_404() throws Exception {
        when(trainerService.findByUsername("asror.r"))
                .thenThrow(new EntityNotFoundException("Trainer with username asror.r not found"));

        mockMvc.perform(MockMvcRequestBuilders
                .get(endpoint + "/profiles/{username}", "asror.r")
                .header("Authorization", "Bearer " + jwtService.generateToken("asror.r"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void update_ShouldReturn_400() throws Exception {

        TraineeUpdateRequestDTO updateDTO = new TraineeUpdateRequestDTO(
                "a.a", "", "", null, new Date(), "T");

        mockMvc.perform(MockMvcRequestBuilders
                .put(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtService.generateToken("q.q"))
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void update_ShouldReturn_200() throws Exception {
        TrainerUpdateRequestDTO updateDTO = new TrainerUpdateRequestDTO(
                "a.a", "a", "a", true, UUID.randomUUID());

        mockMvc.perform(MockMvcRequestBuilders
                .put(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtService.generateToken("a.a"))
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
