package com.example.controller;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.controller.TrainerWorkloadController;
import com.example.demo.dto.ActionType;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.model.TrainerWorkload;
import com.example.demo.security.JwtService;
import com.example.demo.service.TrainerWorkloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(TrainerWorkloadController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrainerWorkloadControllerTest {

    @MockitoBean
    private TrainerWorkloadService workloadService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void signUpTrainee_ShouldReturn_200() throws Exception {
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO("asror", "abror", "r", LocalDate.now(),
                1.5, ActionType.ADD);

        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(MockMvcRequestBuilders.post("/workload").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getMonthlyWorkload_Shouldreturn_200() throws Exception {

        when(workloadService.getTrainerWorkload("asror.r")).thenReturn(new TrainerWorkload());
        mockMvc.perform(MockMvcRequestBuilders.get("/workload/{username}", "asror.r")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
