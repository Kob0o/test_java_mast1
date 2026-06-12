package com.example.exo11.repository;

import com.example.exo11.exception.NotImplementedException;
import com.example.exo11.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryTicketRepository implements TicketRepository {

    @Override
    public Ticket save(Ticket ticket) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public List<Ticket> findAll() {
        throw new NotImplementedException();
    }
}
