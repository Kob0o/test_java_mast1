package com.example.model;

public class ReservationResult {

    private final boolean accepted;

    public ReservationResult(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
