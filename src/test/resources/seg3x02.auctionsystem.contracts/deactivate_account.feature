Feature: De-activate a User Account
  Scenario: Deactivate a user account
    Given the user account is active
    And the user account has no pending payment
    And the user account is not linked to an open auction
    When application command deactivateAccount is invoked
    Then the user account is marked as inactive
