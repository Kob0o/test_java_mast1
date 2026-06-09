Feature: Order refused for unknown product

  Scenario: Unknown product reference
    Given no product exists with reference "UNKNOWN"
    And a customer with email "client@example.com" and profile STANDARD
    When I place an order for 1 unit of "UNKNOWN"
    Then the order should be refused
    And the product repository should have been queried for "UNKNOWN"
