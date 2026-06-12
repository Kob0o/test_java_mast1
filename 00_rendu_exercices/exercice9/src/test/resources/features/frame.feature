Feature: Bowling frame

  Scenario: First roll in a standard frame increases the score
    Given a pin generator
    And a standard frame
    When the next roll knocks down 3 pins with a maximum of 10
    And I make a roll
    Then the roll should be accepted
    And the frame score should be 3

  Scenario: Second roll in a standard frame increases the score
    Given a pin generator
    And a standard frame
    When the next roll knocks down 3 pins with a maximum of 10
    And I make a roll
    And the next roll knocks down 4 pins with a maximum of 7
    And I make a roll
    Then the roll should be accepted
    And the frame score should be 7

  Scenario: Second roll is rejected after a strike in a standard frame
    Given a pin generator
    And a standard frame
    When the next roll knocks down 10 pins with a maximum of 10
    And I make a roll
    And I make a roll
    Then the roll should be rejected
    And the frame score should be 10

  Scenario: Third roll is rejected when a standard frame already has two rolls
    Given a pin generator
    And a standard frame
    When the next roll knocks down 3 pins with a maximum of 10
    And I make a roll
    And the next roll knocks down 4 pins with a maximum of 7
    And I make a roll
    And I make a roll
    Then the roll should be rejected
    And the frame score should be 7

  Scenario: Second roll after a strike in the last frame increases the score
    Given a pin generator
    And a last frame
    And the next rolls with a maximum of 10 knock down 10 and 5 pins
    When I make a roll
    And I make a roll
    Then the roll should be accepted
    And the frame score should be 15

  Scenario: Third roll is accepted when the last frame starts with a strike
    Given a pin generator
    And a last frame
    And the next rolls with a maximum of 10 knock down 10, 5 and 3 pins
    When I make a roll
    And I make a roll
    And I make a roll
    Then the roll should be accepted

  Scenario: Second roll is accepted when the last frame starts with a strike
    Given a pin generator
    And a last frame
    And the next rolls with a maximum of 10 knock down 10 and 5 pins
    When I make a roll
    And I make a roll
    Then the roll should be accepted

  Scenario: Third roll after a strike in the last frame increases the score
    Given a pin generator
    And a last frame
    And the next rolls with a maximum of 10 knock down 10, 5 and 3 pins
    When I make a roll
    And I make a roll
    And I make a roll
    Then the roll should be accepted
    And the frame score should be 18

  Scenario: Third roll is accepted when the last frame starts with a spare
    Given a pin generator
    And a last frame
    And the next rolls knock down 7 pins with max 10 then 3 pins with max 3 then 5 pins with max 10
    When I make a roll
    And I make a roll
    And I make a roll
    Then the roll should be accepted

  Scenario: Third roll after a spare in the last frame increases the score
    Given a pin generator
    And a last frame
    And the next rolls knock down 7 pins with max 10 then 3 pins with max 3 then 5 pins with max 10
    When I make a roll
    And I make a roll
    And I make a roll
    Then the roll should be accepted
    And the frame score should be 15

  Scenario: Third roll is rejected in the last frame without strike or spare
    Given a pin generator
    And a last frame
    When the next roll knocks down 3 pins with a maximum of 10
    And I make a roll
    And the next roll knocks down 4 pins with a maximum of 7
    And I make a roll
    And I make a roll
    Then the roll should be rejected
    And the frame score should be 7

  Scenario: Fourth roll is rejected in the last frame
    Given a pin generator
    And a last frame
    And the next rolls with a maximum of 10 knock down 10, 5 and 3 pins
    When I make a roll
    And I make a roll
    And I make a roll
    And I make a roll
    Then the roll should be rejected
    And the frame score should be 18
