package com.example.exo12.exception;

public class InvalidTimeSlotException extends RuntimeException {

    public InvalidTimeSlotException() {
        super("End time must be after start time");
    }
}
