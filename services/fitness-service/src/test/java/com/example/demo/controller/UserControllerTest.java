package com.example.demo.controller;

import com.example.demo.dao.UserDAO;
import com.example.demo.utils.BruteForceProtectorService;
import org.junit.jupiter.api.BeforeEach;
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
import com.example.demo.dto.request.StatusRequestDTO;
import com.example.demo.security.JwtService;
import com.example.demo.service.TraineeService;
import com.example.demo.service.TrainerService;
import com.google.gson.Gson;

@WebMvcTest(UserStatusController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @MockitoBean
    private UserDAO userDAO;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private TraineeService traineeService;

    @MockitoBean
    private TrainerService trainerService;

    @MockitoBean
    private BruteForceProtectorService bruteForceProtectorService;

    private StatusRequestDTO requestDTO;

    @Autowired
    private Gson gson;

    @BeforeEach
    void initialize() {
        requestDTO = new StatusRequestDTO();
        requestDTO.setUsername("asror");
        requestDTO.setStatus(false);
    }

    @Test
    void statusTrainee_400() throws Exception {
        requestDTO = new StatusRequestDTO();
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/trainees/status", "asror")
                .header("Authorization", "Bearer " + jwtService.generateToken("asror"))
                .content(gson.toJson(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void statusTrainee_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/trainees/status", "asror")
                .header("Authorization", "Bearer " + jwtService.generateToken("asror"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void statusTrainer_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/trainers/status", "asror")
                .header("Authorization", "Bearer " + jwtService.generateToken("asror"))
                .accept(MediaType.APPLICATION_JSON)
                .content(gson.toJson(requestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
