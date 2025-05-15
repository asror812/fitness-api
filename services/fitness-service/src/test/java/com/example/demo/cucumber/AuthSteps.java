package com.example.demo.cucumber;

import io.cucumber.java.en.*;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dto.request.ChangePasswordRequestDTO;
import com.example.demo.dto.request.SignInRequestDTO;
import com.example.demo.dto.request.TraineeSignUpRequestDTO;
import com.example.demo.dto.request.TrainerSignUpRequestDTO;
import com.example.demo.model.TrainingType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
public class AuthSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrainingTypeDAO dao;

    @Autowired
    private StepContext stepContext;

    private TraineeSignUpRequestDTO traineeSignUpRequestDTO;
    private TrainerSignUpRequestDTO trainerSignUpRequestDTO;
    private SignInRequestDTO requestDTO;
    private ChangePasswordRequestDTO changePasswordRequestDTO;

    @Given("valid trainee sign-up credentials")
    public void validTraineeSignUpRequest() {
        traineeSignUpRequestDTO = new TraineeSignUpRequestDTO("asror", "r", new Date(), "T");
    }

    @Given("invalid trainee sign-up credentials")
    public void invalidTraineeSignUpRequest() {
        traineeSignUpRequestDTO = new TraineeSignUpRequestDTO("", "", new Date(), "T");
    }

    @Given("valid trainer sign-up credentials")
    public void validTrainerSignUpRequest() {
        UUID id = dao.create(new TrainingType("swimming-1", Collections.emptyList(), Collections.emptyList())).getId();
        trainerSignUpRequestDTO = new TrainerSignUpRequestDTO("asror", "r", id);
    }

    @Given("invalid trainer sign-up credentials")
    public void invalidTrainerSignUpRequest() {
        UUID id = dao.create(new TrainingType("swimming-2", Collections.emptyList(), Collections.emptyList())).getId();
        trainerSignUpRequestDTO = new TrainerSignUpRequestDTO("", "", id);
    }

    @When("trainee sends a POST request to sign-up {string}")
    public void traineeSignUp(String url) throws Exception {
        ResultActions response = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(traineeSignUpRequestDTO)));
        stepContext.setResponse(response);
    }

    @When("trainer sends a POST request to sign-up {string}")
    public void trainerSignUp(String url) throws Exception {
        ResultActions response = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerSignUpRequestDTO)));
        stepContext.setResponse(response);
    }

    @Then("the response should contain a token")
    public void theResponseShouldContainAToken() throws Exception {
        stepContext.getResponse().andExpect(jsonPath("$.token").exists());
    }

    @Given("{string} sign in credentials")
    public void loginCredentials(String type) throws Exception {
        switch (type) {
            case "valid":
                requestDTO = registerNewUser();
                break;
            case "invalid":
                requestDTO = new SignInRequestDTO("wrong_user", "wrong_pass");
                break;
            default:
                Assertions.fail("Unknown login credential type: " + type);
        }
    }

    @When("user sends a POST request to sign-in {string}")
    public void userSignIn(String url) throws Exception {
        ResultActions response = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)));
        stepContext.setResponse(response);
    }

    @Given("valid change password credentials")
    public void validChangePasswordRequest() throws Exception {
        SignInRequestDTO registerNewUser = registerNewUser();
        changePasswordRequestDTO = new ChangePasswordRequestDTO(registerNewUser.getUsername(),
                registerNewUser.getPassword(),
                "12345678910");
    }

    @Given("invalid change password credentials")
    public void invalidChangePasswordRequest() {
        changePasswordRequestDTO = new ChangePasswordRequestDTO("asror.r", "qwerty", "87654321");
    }

    @When("user sends a PUT request to {string}")
    public void changePassword(String url) throws Exception {
        ResultActions response = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequestDTO)));
        stepContext.setResponse(response);
    }

    private SignInRequestDTO registerNewUser() throws Exception {
        traineeSignUpRequestDTO = new TraineeSignUpRequestDTO("asror", "R", new Date(), "Q");
        String url = "/api/v1/fitness/auth/trainees/sign-up";

        MvcResult mvcResult = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(traineeSignUpRequestDTO)))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String username = jsonNode.get("username").asText();
        String password = jsonNode.get("password").asText();
        return new SignInRequestDTO(username, password);
    }
}
