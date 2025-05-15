package com.example.demo.cucumber;

import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.demo.dao.TrainerDAO;
import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dto.request.TrainerSignUpRequestDTO;
import com.example.demo.dto.request.TrainerUpdateRequestDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.model.TrainingType;
import com.example.demo.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;

@Transactional
public class TrainerSteps {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMaper;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private StepContext stepContext;

    private String username;

    private TrainerUpdateRequestDTO updateRequestDTO;

    @Autowired
    private TrainerDAO dao;

    @Autowired
    private TrainingTypeDAO dTypeDAO;

    @Given("existing trainer username")
    public void validGetTrainerByProfileRequest() {
        UUID id = dTypeDAO.create(new TrainingType("swimming-2", Collections.emptyList(), Collections.emptyList()))
                .getId();

        SignUpResponseDTO register = trainerService.register(
                new TrainerSignUpRequestDTO("asror", "abror", id));
        username = register.getUsername();
    }

    @Given("non existing trainer username")
    public void invalidGetTrainerByProfileRequest() {
        username = "wrong_user";
    }

    @Given("valid trainer update data")
    public void valid_update_data() {
        UUID id = dTypeDAO.create(new TrainingType("swimming-2", Collections.emptyList(), Collections.emptyList())).getId();

        updateRequestDTO = new TrainerUpdateRequestDTO(username, "asror", "R", true, id);
    }

    @Given("invalid trainer update data")
    public void invalid_update_data() {
        UUID id = dTypeDAO.create(new TrainingType("swimming-2", Collections.emptyList(), Collections.emptyList()))
                .getId();
        updateRequestDTO = new TrainerUpdateRequestDTO(username, "", "", true, id);
    }

    @When("trainer sends a GET request to get profile {string}")
    public void getTraineeByProfile(String url) throws Exception {
        url = url + username;
        ResultActions response = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
        stepContext.setResponse(response);
    }

    @And("the response should return trainer response DTO")
    public void theResponseShouldContainResponseDTO() throws Exception {
        stepContext.getResponse().andExpect(jsonPath("$.user.firstName").exists());
        stepContext.getResponse().andExpect(jsonPath("$.user.lastName").exists());
    }

    @When("trainer sends a PUT request to update {string}")
    public void trainee_sends_a_put_request_to_update(String url) throws Exception {
        ResultActions response = mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON)
                .content(objectMaper.writeValueAsString(updateRequestDTO)));

        stepContext.setResponse(response);
    }

}
