package com.example.demo.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.dao.UserDAO;
import com.example.demo.utils.BruteForceProtectorService;
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

import com.example.demo.dto.request.TraineeTrainersUpdateRequestDTO;
import com.example.demo.dto.request.TraineeUpdateRequestDTO;
import com.example.demo.dto.response.TraineeResponseDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.dto.response.UserUpdateResponseDTO;
import com.example.demo.security.JwtService;
import com.example.demo.service.TraineeService;
import com.google.gson.Gson;
import io.jsonwebtoken.lang.Collections;

@WebMvcTest(TraineeController.class)
@AutoConfigureMockMvc(addFilters = false)
class TraineeControllerTest {

    @MockitoBean
    private TraineeService traineeService;

    private final String endpoint = "/trainees";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserDAO userDAO;

    @MockitoBean
    private BruteForceProtectorService bruteForceProtectorService;


    @Autowired
    private Gson gson;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void delete_ShouldReturn_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(endpoint + "/{username}", "qwerty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getNotAssignedTrainers_ShouldReturn_200() throws Exception {
        when(traineeService.getNotAssignedTrainers("a.a")).thenReturn(getNotAssignedTrainers());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(endpoint + "/{username}/not-assigned-trainers", "a.a"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }

    @Test
    void getProfile_ShouldReturn_200() throws Exception {
        TraineeResponseDTO traineeResponseDTO = new TraineeResponseDTO(
                new UserResponseDTO("q", "q", true), new Date(), "T", Collections.emptyList());

        when(traineeService.findByUsername("q.q")).thenReturn(Optional.of(traineeResponseDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(endpoint + "/profiles/{username}", "q.q"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.firstName").value("q"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.lastName").value("q"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.active").value("true"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").exists());
    }

    @Test
    void update_ShouldReturn_400() throws Exception {

        TraineeUpdateRequestDTO updateDTO = new TraineeUpdateRequestDTO(
                "a.a", "", "", null, new Date(), "T");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void update_ShouldReturn_200() throws Exception {
            TraineeUpdateRequestDTO updateDTO = new TraineeUpdateRequestDTO(
                            "a.a", "a", "a", true, new Date(), "T");

            mockMvc.perform(MockMvcRequestBuilders
                            .put(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(updateDTO)))
                            .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void updateTraineeAssignedTrainers() throws Exception {
            TraineeTrainersUpdateRequestDTO requestDTO = new TraineeTrainersUpdateRequestDTO(Collections.emptyList());

            mockMvc.perform(MockMvcRequestBuilders
                            .put(endpoint + "/{username}/trainers" , "asror.r")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(requestDTO)))
                            .andExpect(MockMvcResultMatchers.status().isOk()); 
    }

    private List<TrainerResponseDTO> getNotAssignedTrainers() {
        List<TrainerResponseDTO> trainerResponseDTOs = new ArrayList<>();
        trainerResponseDTOs.add(new TrainerResponseDTO(
                new UserUpdateResponseDTO("a.a", "a", "a", true),
                new TrainingTypeResponseDTO(UUID.randomUUID(), "Swimming")));

        trainerResponseDTOs.add(new TrainerResponseDTO(
                new UserUpdateResponseDTO("b", "b", "b", false),
                new TrainingTypeResponseDTO(UUID.randomUUID(), "Cycling")));

        return trainerResponseDTOs;
    }

}
