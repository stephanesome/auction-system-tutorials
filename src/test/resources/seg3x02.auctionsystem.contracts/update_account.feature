Feature: Update a User Account information
  Scenario: Update a User Account with credit card information,  user has pending payment successfully payed off
    Given the user is signed in
    And the user has pending payment
    And the provided account update information includes credit card information
    And the new credit card is able to settle the pending payment
    When the application command updateAccount is invoked
    Then the account properties are modified according to the account update information
    And an updated credit card is created
    And the updated credit card is initialized from the credit card information
    And the updated credit card is set as the user account credit card
    And the pending payment is removed from user account
  Scenario: Update a User Account with credit card information, user does not owe pending payment
    Given the user is signed in
    And the provided account update information includes credit card information
    When the application command updateAccount is invoked
    Then the account properties are modified according to the account update information
    And an updated credit card is created
    And the updated credit card is initialized from the credit card information
    And the updated credit card is set as the user account credit card
  Scenario: Update a User Account with no credit card information
    Given the user is signed in
    And the provided account update information does not include credit card information
    When the application command updateAccount is invoked
    Then the account properties are modified according to the account update information
