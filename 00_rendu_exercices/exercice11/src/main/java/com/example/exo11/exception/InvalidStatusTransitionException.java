package com.example.exo11.exception;

import com.example.exo11.model.TicketStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(TicketStatus current, TicketStatus requested) {
        super("Invalid status transition: " + current + " -> " + requested);
    }
}
