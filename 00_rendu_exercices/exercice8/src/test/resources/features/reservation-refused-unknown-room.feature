Feature: Reservation refused for unknown room

  Scenario: Unknown room code
    Given no room exists with code "UNKNOWN"
    And a user with email "user@example.com"
    When I reserve room "UNKNOWN" for 5 participants from "2026-06-10 09:00" to "2026-06-10 10:00"
    Then the reservation should be refused
    And no confirmation notification should be sent
    And the room repository should have been queried for "UNKNOWN"
