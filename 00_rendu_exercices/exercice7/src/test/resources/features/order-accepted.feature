Feature: Accepted order

  Background:
    Given a product "PROD-001" named "Widget" with unit price 100 and stock 10

  Scenario: Standard customer order without discount
    Given a customer with email "standard@example.com" and profile STANDARD
    When I place an order for 2 units of "PROD-001"
    Then the order should be accepted
    And the receipt product reference should be "PROD-001"
    And the receipt quantity should be 2
    And the receipt total amount should be 200.0
    And the receipt should have a confirmation message
    And the product repository should have been queried for "PROD-001"

  Scenario: Premium customer order with 10 percent discount
    Given a customer with email "premium@example.com" and profile PREMIUM
    When I place an order for 2 units of "PROD-001"
    Then the order should be accepted
    And the receipt product reference should be "PROD-001"
    And the receipt quantity should be 2
    And the receipt total amount should be 180.0
    And the receipt should have a confirmation message
    And the product repository should have been queried for "PROD-001"

  Scenario: VIP customer order with 20 percent discount
    Given a customer with email "vip@example.com" and profile VIP
    When I place an order for 2 units of "PROD-001"
    Then the order should be accepted
    And the receipt product reference should be "PROD-001"
    And the receipt quantity should be 2
    And the receipt total amount should be 160.0
    And the receipt should have a confirmation message
    And the product repository should have been queried for "PROD-001"
