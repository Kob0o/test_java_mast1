package com.example.exercice5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de DiceScore")
class DiceScoreTest {

    @Mock
    private Ide ide;

    private DiceScore diceScore;

    @BeforeEach
    void setUp() {
        diceScore = new DiceScore(ide);
    }

    @Test
    @DisplayName("Retourne la valeur du dé * 2 + 10 quand les deux lancers sont identiques")
    void shouldReturnDoubleValuePlusTenWhenBothRollsAreEqual() {
        when(ide.getRoll()).thenReturn(4, 4);

        int score = diceScore.getScore();

        assertEquals(18, score);
        verify(ide, times(2)).getRoll();
    }

    @Test
    @DisplayName("Retourne 30 quand les deux lancers valent 6")
    void shouldReturnThirtyWhenBothRollsAreSix() {
        when(ide.getRoll()).thenReturn(6, 6);

        int score = diceScore.getScore();

        assertEquals(30, score);
        verify(ide, times(2)).getRoll();
    }

    @Test
    @DisplayName("Retourne le plus grand lancer quand les deux dés sont différents")
    void shouldReturnHighestRollWhenRollsAreDifferent() {
        when(ide.getRoll()).thenReturn(2, 5);

        int score = diceScore.getScore();

        assertEquals(5, score);
        verify(ide, times(2)).getRoll();
    }

    @Test
    @DisplayName("Retourne le premier lancer si il est supérieur au second")
    void shouldReturnFirstRollWhenItIsHigherThanSecond() {
        when(ide.getRoll()).thenReturn(5, 2);

        int score = diceScore.getScore();

        assertEquals(5, score);
        verify(ide, times(2)).getRoll();
    }
}
