package com.example.demo.cucumber;

import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

@Component
public class StepContext {
    private ResultActions response;

    public ResultActions getResponse() {
        return response;
    }

    public void setResponse(ResultActions response) {
        this.response = response;
    }
}
