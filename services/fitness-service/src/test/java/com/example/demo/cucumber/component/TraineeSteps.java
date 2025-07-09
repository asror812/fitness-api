package com.example.demo.cucumber.component;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.example.demo.dto.request.TraineeSignUpRequestDTO;
import com.example.demo.dto.request.TraineeUpdateRequestDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.service.TraineeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;

@Transactional
public class TraineeSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMaper;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private StepContext stepContext;

    private String username;

    private TraineeUpdateRequestDTO updateRequestDTO;

    @Given("existing trainee username")
    public void validGetTraineeByProfileRequest() {
        SignUpResponseDTO register = traineeService.register(
                new TraineeSignUpRequestDTO("asror", "abror", new Date(), "T"));
        username = register.getUsername();
    }

    @Given("non existing trainee username")
    public void invalidGetTraineeByProfileRequest() {
        username = "wrong_user";
    }

    @Given("valid trainee update data")
    public void valid_update_data() {
        updateRequestDTO = new TraineeUpdateRequestDTO(username, "asror", "R", true, new Date(),
                "Tashkent");
    }

    @Given("invalid trainee update data")
    public void invalid_update_data() {
        updateRequestDTO = new TraineeUpdateRequestDTO(username, "", "", true, new Date(),
                "Tashkent");
    }

    @When("trainee sends a GET request to get profile {string}")
    public void getTraineeByProfile(String url) throws Exception {
        url = url + username;
        ResultActions response = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
        stepContext.setResponse(response);
    }

    @And("the response should return trainee response DTO")
    public void theResponseShouldContainResponseDTO() throws Exception {
        stepContext.getResponse().andExpect(jsonPath("$.user.firstName").exists());
        stepContext.getResponse().andExpect(jsonPath("$.user.lastName").exists());
        stepContext.getResponse().andExpect(jsonPath("$.address").exists());
        stepContext.getResponse().andExpect(jsonPath("$.dateOfBirth").exists());
    }

    @When("trainee sends a DELETE request to delete {string}")
    public void deleteTraineeByUsername(String url) throws Exception {
        url = url + username;

        try {

            ResultActions response = mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON));
            stepContext.setResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @When("trainee sends a PUT request to update {string}")
    public void trainee_sends_a_put_request_to_update(String url) throws Exception {
        ResultActions response = mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)
                .content(objectMaper.writeValueAsString(updateRequestDTO)));

        stepContext.setResponse(response);
    }

    @When("trainee sends a GET request to get not assigned trainers {string}")
    public void getTraineeNotAssignedTrainers(String url) throws Exception {
        url = url + username + "/not-assigned-trainers";
        ResultActions response = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
        stepContext.setResponse(response);
    }

}
