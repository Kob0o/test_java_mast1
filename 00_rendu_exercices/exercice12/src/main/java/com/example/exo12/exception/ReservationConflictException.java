package com.example.exo12.exception;

public class ReservationConflictException extends RuntimeException {

    public ReservationConflictException() {
        super("Time slot overlaps with an existing reservation");
    }
}
