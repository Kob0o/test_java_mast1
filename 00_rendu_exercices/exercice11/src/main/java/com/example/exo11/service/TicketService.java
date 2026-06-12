package com.example.exo11.service;

import com.example.exo11.exception.NotImplementedException;
import com.example.exo11.model.Priority;
import com.example.exo11.model.Ticket;
import com.example.exo11.model.TicketStatus;
import com.example.exo11.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(String title, Priority priority) {
        throw new NotImplementedException();
    }

    public Ticket getTicketById(Long id) {
        throw new NotImplementedException();
    }

    public List<Ticket> getAllTickets() {
        throw new NotImplementedException();
    }

    public Ticket updateStatus(Long id, TicketStatus newStatus) {
        throw new NotImplementedException();
    }
}
