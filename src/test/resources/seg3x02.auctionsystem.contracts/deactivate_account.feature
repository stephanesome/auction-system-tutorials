Feature: De-activate a User Account
  Scenario: Deactivate a user account
    Given the user account is active
    When application command deactivateAccount is invoked
    Then the user account is marked as inactive
