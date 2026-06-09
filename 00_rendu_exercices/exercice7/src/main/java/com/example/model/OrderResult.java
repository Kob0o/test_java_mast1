package com.example.model;

public class OrderResult {

    private final boolean accepted;
    private final OrderReceipt receipt;

    public OrderResult(boolean accepted, OrderReceipt receipt) {
        this.accepted = accepted;
        this.receipt = receipt;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public OrderReceipt getReceipt() {
        return receipt;
    }
}
