package com.example.exercice3;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FibTest {

    @Test
    void shouldReturnNonEmptySeriesWhenRangeIsOne() {
        Fib fib = new Fib(1);

        List<Integer> series = fib.getFibSeries();

        assertFalse(series.isEmpty());
    }

    @Test
    void shouldReturnZeroWhenRangeIsOne() {
        Fib fib = new Fib(1);

        List<Integer> series = fib.getFibSeries();

        assertEquals(List.of(0), series);
    }

    @Test
    void shouldContainThreeWhenRangeIsSix() {
        Fib fib = new Fib(6);

        List<Integer> series = fib.getFibSeries();

        assertTrue(series.contains(3));
    }

    @Test
    void shouldContainSixElementsWhenRangeIsSix() {
        Fib fib = new Fib(6);

        List<Integer> series = fib.getFibSeries();

        assertEquals(6, series.size());
    }

    @Test
    void shouldNotContainFourWhenRangeIsSix() {
        Fib fib = new Fib(6);

        List<Integer> series = fib.getFibSeries();

        assertFalse(series.contains(4));
    }

    @Test
    void shouldReturnExpectedFibonacciSeriesWhenRangeIsSix() {
        Fib fib = new Fib(6);

        List<Integer> series = fib.getFibSeries();

        assertEquals(List.of(0, 1, 1, 2, 3, 5), series);
    }

    @Test
    void shouldReturnSeriesSortedInAscendingOrderWhenRangeIsSix() {
        Fib fib = new Fib(6);

        List<Integer> series = fib.getFibSeries();

        for (int i = 0; i < series.size() - 1; i++) {
            assertTrue(series.get(i) <= series.get(i + 1));
        }
    }
}
