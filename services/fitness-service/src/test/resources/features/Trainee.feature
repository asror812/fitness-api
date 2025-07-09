Feature: Trainee info

  Scenario: Get Existing Trainee By Username
    Given existing trainee username
    When trainee sends a GET request to get profile "/api/v1/fitness/trainees/profiles/"
    Then the response status should be 200
    And the response should return trainee response DTO

  Scenario: Get NonExisting Trainee By Username
    Given non existing trainee username
    When trainee sends a GET request to get profile "/api/v1/fitness/trainees/profiles/"
    Then the response status should be 404
  
  Scenario: Delete Existing Trainee By Username
    Given existing trainee username
    When trainee sends a DELETE request to delete "/api/v1/fitness/trainees/"
    Then the response status should be 200
    
  Scenario: Delete NonExisting Trainee By Username
    Given non existing trainee username
    When trainee sends a DELETE request to delete "/api/v1/fitness/trainees/"
    Then  the response status should be 404

  Scenario: Update Existing Trainee
    Given existing trainee username
    And valid trainee update data
    When trainee sends a PUT request to update "/api/v1/fitness/trainees"
    Then the response status should be 200

  Scenario: Update NonExisting Trainee
    Given non existing trainee username
    And valid trainee update data
    When trainee sends a PUT request to update "/api/v1/fitness/trainees"
    Then the response status should be 404

  Scenario: Update Existing Trainee with invalid data
    Given existing trainee username
    And invalid trainee update data
    When trainee sends a PUT request to update "/api/v1/fitness/trainees"
    Then the response status should be 400

  Scenario: Get Not Assigned Trainers of Existing Trainee
    Given existing trainee username
    When trainee sends a GET request to get not assigned trainers "/api/v1/fitness/trainees/"
    Then the response status should be 200

  Scenario: Get Not Assigned Trainers of NonExisting Trainee
    Given non existing trainee username
    When trainee sends a GET request to get not assigned trainers "/api/v1/fitness/trainees/"
    Then the response status should be 404


