Feature: Trainer info

  Scenario: Get Existing Trainer By Username
    Given existing trainer username
    When trainer sends a GET request to get profile "/api/v1/fitness/trainers/profiles/"
    Then the response status should be 200
    And the response should return trainer response DTO

  Scenario: Get NonExisting Trainer By Username
    Given non existing trainer username
    When trainer sends a GET request to get profile "/api/v1/fitness/trainers/profiles/"
    Then the response status should be 404

  Scenario: Update Existing Trainer
    Given existing trainer username
    And valid trainer update data
    When trainer sends a PUT request to update "/api/v1/fitness/trainers"
    Then the response status should be 200

  Scenario: Update NonExisting Trainer
    Given non existing trainer username
    And valid trainer update data
    When trainer sends a PUT request to update "/api/v1/fitness/trainers"
    Then the response status should be 404

  Scenario: Update Existing Trainer with invalid data
    Given existing trainer username
    And invalid trainer update data
    When trainer sends a PUT request to update "/api/v1/fitness/trainers"
    Then the response status should be 400


