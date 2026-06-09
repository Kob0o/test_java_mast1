Feature: Reservation refused for insufficient capacity

  Background:
    Given a room "A04" named "Salle Alpha" with capacity 10

  Scenario: Participant count exceeds room capacity
    Given a user with email "user@example.com"
    When I reserve room "A04" for 15 participants from "2026-06-10 09:00" to "2026-06-10 10:00"
    Then the reservation should be refused
    And no confirmation notification should be sent
    And the room repository should have been queried for "A04"
    And the reservation repository should have been queried for room "A04"
