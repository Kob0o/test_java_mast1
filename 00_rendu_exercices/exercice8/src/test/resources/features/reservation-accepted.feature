Feature: Accepted reservation

  Background:
    Given a room "A04" named "Salle Alpha" with capacity 10

  Scenario: Successful reservation
    Given a user with email "user@example.com"
    When I reserve room "A04" for 5 participants from "2026-06-10 09:00" to "2026-06-10 10:00"
    Then the reservation should be accepted
    And a confirmation notification should be sent to "user@example.com"
    And the room repository should have been queried for "A04"
    And the reservation repository should have been queried for room "A04"

  Scenario: Reservation at maximum capacity
    Given a user with email "user@example.com"
    When I reserve room "A04" for 10 participants from "2026-06-10 09:00" to "2026-06-10 10:00"
    Then the reservation should be accepted
    And a confirmation notification should be sent to "user@example.com"
    And the room repository should have been queried for "A04"
    And the reservation repository should have been queried for room "A04"

  Scenario: Reservation starting after an existing reservation
    Given an existing reservation from "2026-06-10 09:00" to "2026-06-10 10:00" for room "A04"
    And a user with email "user@example.com"
    When I reserve room "A04" for 5 participants from "2026-06-10 10:00" to "2026-06-10 11:00"
    Then the reservation should be accepted
    And a confirmation notification should be sent to "user@example.com"
    And the room repository should have been queried for "A04"
    And the reservation repository should have been queried for room "A04"
