Feature: Meeting room reservations

  Scenario: Reservation is accepted when the slot is free
    Given a room "A101" exists with capacity 10
    When I book the room for "Alice" from "2026-06-25T10:00:00" to "2026-06-25T11:00:00"
    Then the HTTP response status should be 201
    And the response contains the name "Alice"

  Scenario: Reservation is rejected when the room does not exist
    Given no room exists in the API
    When I book room 99 for "Bob" from "2026-06-25T10:00:00" to "2026-06-25T11:00:00"
    Then the HTTP response status should be 404

  Scenario: Reservation is rejected when the slot overlaps
    Given a room "B202" exists with capacity 8
    And the room is already booked from "2026-06-25T10:00:00" to "2026-06-25T11:00:00"
    When I book the room for "Charlie" from "2026-06-25T10:30:00" to "2026-06-25T11:30:00"
    Then the HTTP response status should be 409
