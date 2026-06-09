Feature: Reservation refused for invalid period

  Background:
    Given a room "A04" named "Salle Alpha" with capacity 10

  Scenario: End date is before start date
    Given a user with email "user@example.com"
    When I reserve room "A04" for 5 participants from "2026-06-10 10:00" to "2026-06-10 09:00"
    Then the reservation should be refused
    And no confirmation notification should be sent
    And the room repository should have been queried for "A04"

  Scenario: End date equals start date
    Given a user with email "user@example.com"
    When I reserve room "A04" for 5 participants from "2026-06-10 09:00" to "2026-06-10 09:00"
    Then the reservation should be refused
    And no confirmation notification should be sent
    And the room repository should have been queried for "A04"
