package com.example.demo.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.dao.TrainerWorkloadRepository;
import com.example.demo.model.TrainerWorkload;
import com.example.demo.security.JwtService;
import com.example.demo.service.TrainerWorkloadService;


@WebMvcTest(TrainerWorkloadController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrainerWorkloadControllerTest {

    @MockitoBean
    private TrainerWorkloadService workloadService;

    @MockitoBean
    private TrainerWorkloadRepository repository;

    @MockitoBean
    private JwtService jwtService;

    private final String endpoint = "/api/v1/workloads";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMonthlyWorkload_Shouldreturn_200() throws Exception {
        when(workloadService.getTrainerWorkload("asror.r")).thenReturn(new TrainerWorkload());
        mockMvc.perform(MockMvcRequestBuilders.get(endpoint + "/{username}", "asror.r")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
