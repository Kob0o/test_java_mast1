package com.example.exo11.controller;

import com.example.exo11.dto.CreateTicketRequest;
import com.example.exo11.dto.TicketResponse;
import com.example.exo11.dto.UpdateStatusRequest;
import com.example.exo11.model.Ticket;
import com.example.exo11.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody CreateTicketRequest request) {
        Ticket created = ticketService.createTicket(request.title(), request.priority());
        TicketResponse response = TicketResponse.from(created);

        return ResponseEntity
                .created(URI.create("/api/tickets/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(TicketResponse.from(ticket));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> findAll() {
        List<TicketResponse> responses = ticketService.getAllTickets()
                .stream()
                .map(TicketResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        Ticket updated = ticketService.updateStatus(id, request.status());
        return ResponseEntity.ok(TicketResponse.from(updated));
    }
}
