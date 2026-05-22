package com.example.exercice2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceCalculatorTest {

    private PriceCalculator priceCalculator;

    @BeforeEach
    void setUp() {
        priceCalculator = new PriceCalculator();
    }

    @Test
    void shouldCalculateTotalPrice() {
        double totalPrice = priceCalculator.calculateTotalPrice(10.0, 3);

        assertEquals(30.0, totalPrice);
    }

    @Test
    void shouldApplyDiscount() {
        double discountedPrice = priceCalculator.applyDiscount(100.0, 0.20);

        assertEquals(80.0, discountedPrice);
    }

    @Test
    void shouldCalculateVat() {
        double vat = priceCalculator.calculateVat(100.0, 0.20);

        assertEquals(20.0, vat);
    }

    @Test
    void shouldCalculatePriceWithVat() {
        double priceWithVat = priceCalculator.calculatePriceWithVat(100.0, 0.20);

        assertEquals(120.0, priceWithVat);
    }

    @Test
    void shouldThrowWhenUnitPriceIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculateTotalPrice(-1.0, 1)
        );

        assertEquals("Le prix unitaire ne doit pas être négatif.", exception.getMessage());
    }

    @Test
    void shouldThrowWhenQuantityIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculateTotalPrice(10.0, -1)
        );

        assertEquals("La quantité ne doit pas être négative.", exception.getMessage());
    }

    @Test
    void shouldThrowWhenDiscountRateIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.applyDiscount(100.0, -0.1)
        );

        assertEquals("Le taux de remise ne doit pas être négatif.", exception.getMessage());
    }

    @Test
    void shouldThrowWhenVatRateIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculateVat(100.0, -0.1)
        );

        assertEquals("Le taux de TVA ne doit pas être négatif.", exception.getMessage());
    }

    @Test
    void shouldThrowWhenVatRateIsNegativeInPriceWithVat() {
        assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculatePriceWithVat(100.0, -0.1)
        );
    }
}
