Feature: Add new auction by Seller.
  Scenario: Seller is signed in, Seller has no Pending Payment, Auction information doesn't include Credit card information
    Given the seller is signed in
    And the seller has no pending payment
    And the seller has a credit card
    And auction information is provided
    And the auction information does not include credit card information
    When the application command addAuction is invoked
    Then a new auction is created
    And the new auction is initialized from the auction information
    And the new auction processing fee has been set
    And the new auction is added to the seller's auctions
    And a new item is created
    And the new item is initialized from the auction information
    And the new auction is linked to the new item
  Scenario: Seller is signed in, Seller has no Pending Payment, Auction information includes Credit card information
    Given the seller is signed in
    And the seller has no pending payment
    And auction information is provided
    And the auction information includes credit card information
    When the application command addAuction is invoked
    Then a new auction is created
    And the new auction is initialized from the auction information
    And the new auction processing fee has been set
    And the new auction is added to the seller's auctions
    And a new item is created
    And the new item is initialized from the auction information
    And the new auction is linked to the new item
    And a new credit card is created
    And the new credit card is initialized from the credit card information
    And the new credit card is set as the seller credit card
