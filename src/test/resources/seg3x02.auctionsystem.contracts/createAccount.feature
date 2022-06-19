Feature: Create a new User Account (Buyer, Seller)
  Scenario: Create a User Account with credit card information
    Given provided account information doesn't match an existing user account
    And provided account information includes credit card information
    When the application command createAccount is invoked
    Then a new user account is created
    And the new user account is initialized from the account information
    And the new user account is set as active
    And a new credit card is created
    And the new credit card is initialized from the credit card information
    And the new credit card is set as the user account credit card
  Scenario: Create a User Account without credit card information
    Given provided account information doesn't match an existing user account
    And provided account information does not include credit card information
    When the application command createAccount is invoked
    Then a new user account is created
    And the new user account is initialized from the account information
    And the new user account is set as active
