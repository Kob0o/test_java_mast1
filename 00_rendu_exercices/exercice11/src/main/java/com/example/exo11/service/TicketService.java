package com.example.exo11.service;

import com.example.exo11.exception.InvalidStatusTransitionException;
import com.example.exo11.exception.TicketNotFoundException;
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
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setPriority(priority);
        ticket.setStatus(TicketStatus.OPEN);
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket updateStatus(Long id, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        TicketStatus current = ticket.getStatus();

        if (!isValidTransition(current, newStatus)) {
            throw new InvalidStatusTransitionException(current, newStatus);
        }

        ticket.setStatus(newStatus);
        return ticketRepository.save(ticket);
    }

    private boolean isValidTransition(TicketStatus current, TicketStatus newStatus) {
        if (current == TicketStatus.RESOLVED) {
            return false;
        }
        if (current == TicketStatus.OPEN) {
            return newStatus == TicketStatus.IN_PROGRESS || newStatus == TicketStatus.RESOLVED;
        }
        if (current == TicketStatus.IN_PROGRESS) {
            return newStatus == TicketStatus.RESOLVED;
        }
        return false;
    }
}
