package com.example.model;

public class Order {

    private final String customerEmail;
    private final String productReference;
    private final int quantity;

    public Order(String customerEmail, String productReference, int quantity) {
        this.customerEmail = customerEmail;
        this.productReference = productReference;
        this.quantity = quantity;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getProductReference() {
        return productReference;
    }

    public int getQuantity() {
        return quantity;
    }
}
