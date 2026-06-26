Feature: Item reservations

  Scenario: Reserve an unavailable item
    Given item "BOOK-01" is borrowed by member "MEMBER-001"
    When member "MEMBER-002" reserves item "BOOK-01"
    Then the reservation is accepted

  Scenario: Multiple reservations on the same item
    Given item "BOOK-01" is borrowed by member "MEMBER-001"
    When member "MEMBER-002" reserves item "BOOK-01"
    And member "MEMBER-003" reserves item "BOOK-01"
    Then item "BOOK-01" has 2 pending reservations

  Scenario: Return a borrowed item with a reservation
    Given item "BOOK-01" is borrowed by member "MEMBER-001"
    And member "MEMBER-002" has reserved item "BOOK-01"
    When member "MEMBER-001" returns item "BOOK-01"
    Then member "MEMBER-002" has borrowed item "BOOK-01"

  Scenario: Reject reservation for suspended member
    Given member "MEMBER-001" is suspended
    And item "BOOK-01" is borrowed by member "MEMBER-002"
    When member "MEMBER-001" reserves item "BOOK-01"
    Then the reservation is rejected

  Scenario: Reject reservation when item is available
    Given no loan or reservation exists
    When member "MEMBER-001" reserves item "BOOK-01"
    Then the reservation is rejected
