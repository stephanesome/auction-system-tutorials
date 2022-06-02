Feature: Update a User Account information
  Scenario 1: Update a User Account with credit card information
    Given the user account is active
    And the provided account update information includes credit card information
    When the application command updateAccount is invoked
    Then the account information is modified according to the account update information
    And a new credit card is created
    And the new credit card is initialized from the credit card information
    And the new credit card is set as the user account credit card
  Scenario 2: Update a User Account with no credit card information
    Given the user account is active
    And the provided account update information does not include credit card information
    When the application command updateAccount is invoked
    Then the account information is modified according to the account update information
