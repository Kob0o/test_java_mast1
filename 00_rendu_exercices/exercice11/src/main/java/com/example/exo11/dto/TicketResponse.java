package com.example.exo11.dto;

import com.example.exo11.model.Priority;
import com.example.exo11.model.Ticket;
import com.example.exo11.model.TicketStatus;

public record TicketResponse(
        Long id,
        String title,
        Priority priority,
        TicketStatus status
) {

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getPriority(),
                ticket.getStatus()
        );
    }
}
