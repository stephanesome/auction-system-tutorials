Feature: Buyer place bid on an auction
  Scenario: Buyer is signed in, Buyer has no Pending Payment
    Given the buyer is signed in
    And the buyer has no pending payment
    And the auction is open
    And bid information is provided
    When the application command placeBid is executed
    Then a new bid is created
    And the new is initialized from the bid information
    And the new bid is added to the auction bids
    And the new bid is added to the buyer bids
