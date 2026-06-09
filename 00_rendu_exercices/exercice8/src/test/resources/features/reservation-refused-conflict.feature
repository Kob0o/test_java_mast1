Feature: Reservation refused for scheduling conflict

  Background:
    Given a room "A04" named "Salle Alpha" with capacity 10
    And an existing reservation from "2026-06-10 09:00" to "2026-06-10 10:00" for room "A04"

  Scenario: Requested slot overlaps an existing reservation
    Given a user with email "user@example.com"
    When I reserve room "A04" for 5 participants from "2026-06-10 09:30" to "2026-06-10 10:30"
    Then the reservation should be refused
    And no confirmation notification should be sent
    And the room repository should have been queried for "A04"
    And the reservation repository should have been queried for room "A04"
