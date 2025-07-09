Feature: Training integration test

  Feature: Training Integration Test

  Scenario: Successful Training session creation
    Given existing trainee and trainer for training
    When trainee sends a POST request to create training "/api/v1/fitness/trainings"
    Then the response status should be 201
    And a JMS message should be sent with action type ADD
