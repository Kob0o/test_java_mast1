package com.example.exo11.repository;

import com.example.exo11.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTicketRepository implements TicketRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Ticket> tickets = new ConcurrentHashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(sequence.incrementAndGet());
        }
        tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return Optional.ofNullable(tickets.get(id));
    }

    @Override
    public List<Ticket> findAll() {
        return new ArrayList<>(tickets.values())
                .stream()
                .sorted(Comparator.comparing(Ticket::getId))
                .toList();
    }

    @Override
    public void deleteAll() {
        tickets.clear();
        sequence.set(0);
    }
}
