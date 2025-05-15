package com.example.demo.cucumber;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Then;

public class CommonSteps {

    private final StepContext stepContext;

    public CommonSteps(StepContext stepContext) {
        this.stepContext = stepContext;
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) throws Exception {
        stepContext.getResponse().andExpect(status().is(statusCode));
    }
}
