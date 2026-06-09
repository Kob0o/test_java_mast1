package com.example.exercice2;

public class PriceCalculator {

    public double calculateTotalPrice(double unitPrice, int quantity) {
        return computeTotalPrice(unitPrice, quantity);
    }

    public double applyDiscount(double price, double discountRate) {
        return computeDiscount(price, discountRate);
    }

    public double calculateVat(double price, double vatRate) {
        return computeVat(price, vatRate);
    }

    public double calculatePriceWithVat(double price, double vatRate) {
        return computePriceWithVat(price, vatRate);
    }

    private double computeTotalPrice(double unitPrice, int quantity) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Le prix unitaire ne doit pas être négatif.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("La quantité ne doit pas être négative.");
        }
        return unitPrice * quantity;
    }

    private double computeDiscount(double price, double discountRate) {
        if (discountRate < 0) {
            throw new IllegalArgumentException("Le taux de remise ne doit pas être négatif.");
        }
        return price * (1 - discountRate);
    }

    private double computeVat(double price, double vatRate) {
        if (vatRate < 0) {
            throw new IllegalArgumentException("Le taux de TVA ne doit pas être négatif.");
        }
        return price * vatRate;
    }

    private double computePriceWithVat(double price, double vatRate) {
        return price + computeVat(price, vatRate);
    }
}
