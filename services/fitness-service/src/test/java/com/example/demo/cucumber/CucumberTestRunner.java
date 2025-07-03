package com.example.demo.cucumber;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;

@RunWith(io.cucumber.junit.Cucumber.class)
@CucumberOptions(plugin = { "pretty",
                "html:target/cucumber-report.html" }, features = "src/test/resources/features", glue = "com.example.demo.cucumber")
        
public class CucumberTestRunner {

}
