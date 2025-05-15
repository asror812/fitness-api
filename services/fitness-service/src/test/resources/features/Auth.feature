Feature: Authentication and Registration

  Scenario: Successfull trainee sign-up
    Given valid trainee sign-up credentials
    When trainee sends a POST request to sign-up "/api/v1/fitness/auth/trainees/sign-up"
    Then the response status should be 201
    And the response should contain a token

  Scenario: Trainee sign-up with invalid credentials
    Given invalid trainee sign-up credentials
    When trainee sends a POST request to sign-up "/api/v1/fitness/auth/trainees/sign-up"
    Then the response status should be 400

  Scenario: Succesfull trainer sign-up
    Given valid trainer sign-up credentials
    When trainer sends a POST request to sign-up "/api/v1/fitness/auth/trainers/sign-up"
    Then the response status should be 201
    And the response should contain a token
  
  Scenario: Trainer sign-up with invalid credentials
    Given invalid trainer sign-up credentials
    When trainer sends a POST request to sign-up "/api/v1/fitness/auth/trainers/sign-up"
    Then the response status should be 400

  Scenario Outline: Sing-in attempts
    Given "<credentials>" sign in credentials
    When user sends a POST request to sign-in "/api/v1/fitness/auth/sign-in"
    Then the response status should be <status>

    Examples:
      | credentials | status |
      | valid       | 200    |
      | invalid     | 401    |


  Scenario: Successful password change
    Given valid change password credentials
    When user sends a PUT request to "/api/v1/fitness/auth/change-password"
    Then the response status should be 200
    
  Scenario: Password change with invalid credentials
    Given invalid change password credentials
    When user sends a PUT request to "/api/v1/fitness/auth/change-password"
    Then the response status should be 400
