Feature: Bank account API

  Scenario: Create a new account
    Given no account exists in the API
    When I create an account with number "ACC-001" and holder "Alice"
    Then the HTTP response status should be 201
    And the response balance should be 0

  Scenario: Deposit money on an account
    Given an account "ACC-001" exists for holder "Alice"
    When I deposit 100 on account "ACC-001"
    Then the HTTP response status should be 200
    And the response balance should be 100

  Scenario: Withdraw with sufficient funds
    Given an account "ACC-001" exists for holder "Alice" with balance 200
    When I withdraw 50 from account "ACC-001"
    Then the HTTP response status should be 200
    And the response balance should be 150

  Scenario: Withdraw with insufficient funds
    Given an account "ACC-001" exists for holder "Alice" with balance 30
    When I withdraw 100 from account "ACC-001"
    Then the HTTP response status should be 409

  Scenario: Transfer between two accounts
    Given an account "ACC-001" exists for holder "Alice" with balance 500
    And another account "ACC-002" exists for holder "Bob" with balance 0
    When I transfer 150 from "ACC-001" to "ACC-002"
    Then the HTTP response status should be 200
    And account "ACC-001" has balance 350
    And account "ACC-002" has balance 150

  Scenario: Transfer rejected for insufficient balance
    Given an account "ACC-001" exists for holder "Alice" with balance 50
    And another account "ACC-002" exists for holder "Bob" with balance 0
    When I transfer 100 from "ACC-001" to "ACC-002"
    Then the HTTP response status should be 409
