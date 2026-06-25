package com.example.exo12.exception;

public class ReservationAlreadyCancelledException extends RuntimeException {

    public ReservationAlreadyCancelledException() {
        super("Reservation is already cancelled");
    }
}
