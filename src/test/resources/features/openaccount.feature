Feature: Open a new account

  Scenario: Successfully open a new account with valid information
    Given I provide a valid phone number
    When I submit the account opening request
    Then the account should be opened successfully
