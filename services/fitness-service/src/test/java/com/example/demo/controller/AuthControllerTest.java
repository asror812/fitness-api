package com.example.demo.controller;

import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.UUID;

import com.example.demo.dao.UserDAO;
import com.example.demo.utils.BruteForceProtectorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.demo.dto.request.ChangePasswordRequestDTO;
import com.example.demo.dto.request.SignInRequestDTO;
import com.example.demo.dto.request.TraineeSignUpRequestDTO;
import com.example.demo.dto.request.TrainerSignUpRequestDTO;
import com.example.demo.dto.response.SignInResponseDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.security.JwtService;
import com.example.demo.service.AuthService;
import com.example.demo.service.TraineeService;
import com.example.demo.service.TrainerService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @MockitoBean
    private TraineeService traineeService;

    @MockitoBean
    private TrainerService trainerService;

    @MockitoBean
    private AuthService authService;

   /* @MockitoBean
    private RequestCountInSignUpMetrics requestCountInSignUpMetrics;*/

    @MockitoBean
    private BruteForceProtectorService bruteForceProtectorService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserDAO userDAO;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    private final String endpoint = "/auth";

    @Test
    void signUpTrainee_ShouldReturn_201() throws Exception {
        TraineeSignUpRequestDTO requestDTO = new TraineeSignUpRequestDTO("asror", "abror", new Date(),
                "Tashkent");

        when(traineeService
                .register(Mockito.any(TraineeSignUpRequestDTO.class)))
                .thenReturn(new SignUpResponseDTO("asror.abror", "1234567890", "a"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(endpoint + "/trainees/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("a"));
    }

    @Test
    void signUpTrainee_ShouldReturn_400() throws Exception {
        TraineeSignUpRequestDTO requestDTO = new TraineeSignUpRequestDTO("", "", new Date(), "T");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(endpoint + "/trainees/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void changePassword_ShouldReturn_200() throws Exception {
        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO("a.a", "qwerty12345", "12345678910");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(endpoint + "/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void changePassword_ShouldReturn_400() throws Exception {
        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO("", "", "123456");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(endpoint + "/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void signIn_ShouldReturn_200() throws Exception {
        SignInRequestDTO requestDTO = new SignInRequestDTO("a.a", "qwerty12345");
        when(authService.login(Mockito.any(SignInRequestDTO.class))).thenReturn(new SignInResponseDTO("a"));

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint + "/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token")
                        .value("a"));
    }

    @Test
    void signUpTrainer_ShouldReturn_201() throws Exception {
        TrainerSignUpRequestDTO requestDTO = new TrainerSignUpRequestDTO("a", "a", UUID.randomUUID());

        when(trainerService.register(
                Mockito.any(TrainerSignUpRequestDTO.class)))
                .thenReturn(new SignUpResponseDTO("a.a", "1234567890000", "a"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(endpoint + "/trainers/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token")
                        .value("a"));
    }

    @Test
    void signUpTrainer_ShouldReturn_400() throws Exception {
        TrainerSignUpRequestDTO requestDTO = new TrainerSignUpRequestDTO("", "", UUID.randomUUID());

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint + "/trainers/sign-up")
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

   /*  @Test
    void signUpTrainee_ShouldIncrementRequestCounter() throws Exception {
        TraineeSignUpRequestDTO requestDTO = new TraineeSignUpRequestDTO("asror", "abror", new Date(),
                "Tashkent");

        when(traineeService
                        .register(Mockito.any(TraineeSignUpRequestDTO.class)))
                                        .thenReturn(new SignUpResponseDTO("asror.abror", "1234567890", "a"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(endpoint + "/trainees/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(requestCountInSignUpMetrics, times(1)).increment();
    }
        

    @Test
    void signUpTrainer_ShouldIncrementRequestCounter() throws Exception {
        TrainerSignUpRequestDTO requestDTO = new TrainerSignUpRequestDTO("asror", "abror", UUID.randomUUID());

        when(trainerService.register(any(TrainerSignUpRequestDTO.class))).thenReturn(new SignUpResponseDTO());

        mockMvc.perform(MockMvcRequestBuilders
                        .post(endpoint + "/trainers/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(requestCountInSignUpMetrics, times(1)).increment();
    }
        */

}
