Feature: Order refused for insufficient stock

  Background:
    Given a product "PROD-001" named "Widget" with unit price 100 and stock 3

  Scenario: Requested quantity exceeds available stock
    Given a customer with email "client@example.com" and profile STANDARD
    When I place an order for 5 units of "PROD-001"
    Then the order should be refused
    And the product repository should have been queried for "PROD-001"
