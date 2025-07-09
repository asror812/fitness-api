package com.example.demo.cucumber.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.demo.cucumber.component.StepContext;
import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dto.request.ActionType;
import com.example.demo.dto.request.TraineeSignUpRequestDTO;
import com.example.demo.dto.request.TrainerSignUpRequestDTO;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.model.TrainingType;
import com.example.demo.service.TraineeService;
import com.example.demo.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;

@Transactional
public class TrainingSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    @Value("${jms.workload_queue.update}")
    private String updateTrainerWorkloadQueue;

    @Autowired
    private StepContext stepContext;

    @Autowired
    private TestJmsListener testJmsListener;

    private String traineeUsername;
    private String trainerUsername;

    private String token;

    @Given("existing trainee and trainer for training")
    public void createTraineeAndTrainer() {
        SignUpResponseDTO trainee = traineeService.register(
                new TraineeSignUpRequestDTO("trainee-firtsname", "trainee-lastname",
                        new java.util.Date(), "Tashkent"));
        traineeUsername = trainee.getUsername();

        UUID id = trainingTypeDAO
                .create(new TrainingType("swimming", Collections.emptyList(), Collections.emptyList()))
                .getId();

        SignUpResponseDTO trainer = trainerService.register(
                new TrainerSignUpRequestDTO("trainer-firstname", "trainer-lastname", id));
        trainerUsername = trainer.getUsername();
        token = trainer.getToken();
    }

    @When("trainee sends a POST request to create training {string}")
    public void sendPostRequestToCreateTraining(String url) throws Exception {
        String request = objectMapper
                .writeValueAsString(new com.example.demo.dto.request.TrainingCreateRequestDTO(
                        traineeUsername,
                        trainerUsername,
                        "Swimming Part 2",
                        new Date(2004, 12, 12),
                        2d));

        ResultActions response = mockMvc.perform(post(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));

        stepContext.setResponse(response);
    }

    @Then("a JMS message should be sent with action type ADD")
    public void checkJmsMessageSent() throws Exception {

        TrainerWorkloadRequestDTO message = testJmsListener.awaitMessage(3000);
        assertThat(message).isNotNull();
        assertThat(message.getTrainerUsername()).isEqualTo("trainer1");
        assertThat(message.getActionType()).isEqualTo(ActionType.ADD);
    }

}
