Feature: Update a User Account information
  Scenario 1: Update a User Account with credit card information,  user has pending payment successfully payed off
    Given the user is signed in
    And the user has pending payment
    And the provided account update information includes credit card information
    And the new credit card is able to settle the pending payment
    When the application command updateAccount is invoked
    Then the account properties are modified according to the account update information
    And a new credit card is created
    And the new credit card is initialized from the credit card information
    And the new credit card is set as the user account credit card
    And the pending payment is removed from user account
  Scenario 2: Update a User Account with credit card information, user does not owe pending payment
    Given the user is signed in
    And the provided account update information includes credit card information
    When the application command updateAccount is invoked
    Then the account properties are modified according to the account update information
    And a new credit card is created
    And the new credit card is initialized from the credit card information
    And the new credit card is set as the user account credit card
  Scenario 3: Update a User Account with no credit card information
    Given the user is signed in
    And the provided account update information does not include credit card information
    When the application command updateAccount is invoked
    Then the account properties are modified according to the account update information
