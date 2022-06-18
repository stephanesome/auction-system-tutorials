Feature: Close auction at deadline
  Scenario: Successful auction fee payment
    Given the auction is open
    And the auction deadline has passed
    And the auction seller credit card is able to settle the auction fee
    When the application command closeAuction is invoked
    Then the auction is marked as closed
  Scenario: Unsuccessful auction fee payment
    Given the auction is open
    And the auction deadline has passed
    And the auction seller credit card is unable to settle the auction fee
    When the application command closeAuction is invoked
    Then the auction is marked as closed
    And the auction fee is set as pending payment to the auction seller account
