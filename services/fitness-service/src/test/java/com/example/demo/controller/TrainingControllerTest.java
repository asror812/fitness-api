package com.example.demo.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.demo.dto.request.TrainingCreateRequestDTO;
import com.example.demo.dto.response.TrainingResponseDTO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.JwtService;
import com.example.demo.service.TrainingService;
import com.google.gson.Gson;

@WebMvcTest(TrainingController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrainingControllerTest {

        @Mock
        private JwtService jwtService;

        @MockitoBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Autowired
        private Gson gson;

        @MockitoBean
        private TrainingService trainingService;

        @Autowired
        private MockMvc mockMvc;

        @Test
        void addTraining_ShouldReturn_200() throws Exception {
                TrainingCreateRequestDTO createDTO = new TrainingCreateRequestDTO("a", "a", "Swimming-1", new Date(),
                                1.5);

                mockMvc.perform(MockMvcRequestBuilders.post("/trainings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + jwtService.generateToken("a.a"))
                                .content(gson.toJson(createDTO)))
                                .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        @Test
        void deleteTraining_ShouldReturn_200() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.delete("/trainings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + jwtService.generateToken("a.a"))
                                .content(gson.toJson(UUID.randomUUID())))
                                .andExpect(MockMvcResultMatchers.status().isOk());          
        }

        @Test
        void getTraineeTrainings_ShouldReturn_200() throws Exception {
                when(trainingService.getTraineeTrainings("asror.r", null, null, null, null))
                                .thenReturn(getAllTraineeTrainings());

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/trainings/trainee/{username}", "asror.r")
                                .header("Authorization", "Bearer " + jwtService.generateToken("a.a"))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1));

        }

        private List<TrainingResponseDTO> getAllTraineeTrainings() {
                List<TrainingResponseDTO> list = new ArrayList<>();
                list.add(new TrainingResponseDTO("Swimming-1", new Date(),
                                new TrainingTypeResponseDTO(UUID.randomUUID(), "qwerty"), 1.5));
                return list;
        }

        @Test
        void getTrainerTrainings_ShouldReturn_400() throws Exception {
                when(trainingService.getTrainerTrainings("asror.r", null, null, null))
                                .thenReturn(getAllTrainerTrainings());

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/trainings/trainer/{username}", "asror.r")
                                .header("Authorization", "Bearer " + jwtService.generateToken("a.a"))
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
        }

        private List<TrainingResponseDTO> getAllTrainerTrainings() {
                List<TrainingResponseDTO> list = new ArrayList<>();
                list.add(new TrainingResponseDTO("Swimming-1", new Date(),
                                new TrainingTypeResponseDTO(UUID.randomUUID(), "qwerty"), 1.5));

                list.add(new TrainingResponseDTO("Swimming-1", new Date(),
                                new TrainingTypeResponseDTO(UUID.randomUUID(), "qwerty"), 1.5));
                return list;
        }
}
