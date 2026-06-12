package com.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FrameSteps {

    private IGenerateur generateur;
    private Frame frame;
    private boolean lastRollAccepted;

    @Given("a pin generator")
    public void aPinGenerator() {
        generateur = mock(IGenerateur.class);
    }

    @Given("a standard frame")
    public void aStandardFrame() {
        frame = new Frame(generateur, false);
    }

    @Given("a last frame")
    public void aLastFrame() {
        frame = new Frame(generateur, true);
    }

    @When("the next roll knocks down {int} pins with a maximum of {int}")
    public void theNextRollKnocksDownPinsWithAMaximumOf(int pins, int max) {
        when(generateur.randomPin(max)).thenReturn(pins);
    }

    @Given("the next rolls with a maximum of {int} knock down {int} and {int} pins")
    public void theNextRollsWithAMaximumOfKnockDownTwoPins(int max, int firstPins, int secondPins) {
        when(generateur.randomPin(max)).thenReturn(firstPins, secondPins);
    }

    @Given("the next rolls with a maximum of {int} knock down {int}, {int} and {int} pins")
    public void theNextRollsWithAMaximumOfKnockDownThreePins(int max, int firstPins, int secondPins, int thirdPins) {
        when(generateur.randomPin(max)).thenReturn(firstPins, secondPins, thirdPins);
    }

    @Given("the next rolls knock down {int} pins with max {int} then {int} pins with max {int} then {int} pins with max {int}")
    public void theNextRollsKnockDownWithDifferentMaximums(
            int firstPins, int firstMax,
            int secondPins, int secondMax,
            int thirdPins, int thirdMax) {
        when(generateur.randomPin(firstMax)).thenReturn(firstPins, thirdPins);
        when(generateur.randomPin(secondMax)).thenReturn(secondPins);
    }

    @When("I make a roll")
    public void iMakeARoll() {
        lastRollAccepted = frame.makeRoll();
    }

    @Then("the roll should be accepted")
    public void theRollShouldBeAccepted() {
        assertTrue(lastRollAccepted);
    }

    @Then("the roll should be rejected")
    public void theRollShouldBeRejected() {
        assertFalse(lastRollAccepted);
    }

    @Then("the frame score should be {int}")
    public void theFrameScoreShouldBe(int expectedScore) {
        assertEquals(expectedScore, frame.getScore());
    }
}
