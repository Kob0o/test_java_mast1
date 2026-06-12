package com.example.exo11.service;

import com.example.exo11.exception.InvalidStatusTransitionException;
import com.example.exo11.exception.TicketNotFoundException;
import com.example.exo11.model.Priority;
import com.example.exo11.model.Ticket;
import com.example.exo11.model.TicketStatus;
import com.example.exo11.repository.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketService unit tests")
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("Creates a ticket with the provided title and priority")
    void shouldCreateTicketWithTitleAndPriority() {
        // Arrange
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket ticket = invocation.getArgument(0);
            ticket.setId(1L);
            return ticket;
        });

        // Act
        Ticket result = ticketService.createTicket("Login bug", Priority.HIGH);

        // Assert
        assertEquals("Login bug", result.getTitle());
        assertEquals(Priority.HIGH, result.getPriority());
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Creates a ticket with OPEN status by default")
    void shouldCreateTicketWithOpenStatusByDefault() {
        // Arrange
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket result = ticketService.createTicket("API issue", Priority.MEDIUM);

        // Assert
        assertEquals(TicketStatus.OPEN, result.getStatus());

        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(ticketCaptor.capture());
        assertEquals(TicketStatus.OPEN, ticketCaptor.getValue().getStatus());
    }

    @Test
    @DisplayName("Returns an existing ticket by id")
    void shouldReturnExistingTicketById() {
        // Arrange
        Ticket existingTicket = new Ticket(1L, "Network incident", Priority.LOW, TicketStatus.OPEN);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existingTicket));

        // Act
        Ticket result = ticketService.getTicketById(1L);

        // Assert
        assertEquals(existingTicket, result);
        verify(ticketRepository).findById(1L);
    }

    @Test
    @DisplayName("Throws when ticket does not exist")
    void shouldThrowWhenTicketDoesNotExist() {
        // Arrange
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(99L));
        verify(ticketRepository).findById(99L);
    }

    @Test
    @DisplayName("Returns all tickets")
    void shouldReturnAllTickets() {
        // Arrange
        List<Ticket> tickets = List.of(
                new Ticket(1L, "Ticket A", Priority.LOW, TicketStatus.OPEN),
                new Ticket(2L, "Ticket B", Priority.HIGH, TicketStatus.IN_PROGRESS)
        );
        when(ticketRepository.findAll()).thenReturn(tickets);

        // Act
        List<Ticket> result = ticketService.getAllTickets();

        // Assert
        assertEquals(2, result.size());
        assertEquals(tickets, result);
        verify(ticketRepository).findAll();
    }

    @Test
    @DisplayName("Allows OPEN to IN_PROGRESS transition")
    void shouldAllowTransitionFromOpenToInProgress() {
        // Arrange
        Ticket openTicket = new Ticket(1L, "Open ticket", Priority.MEDIUM, TicketStatus.OPEN);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(openTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket result = ticketService.updateStatus(1L, TicketStatus.IN_PROGRESS);

        // Assert
        assertEquals(TicketStatus.IN_PROGRESS, result.getStatus());
        verify(ticketRepository).save(openTicket);
    }

    @Test
    @DisplayName("Allows OPEN to RESOLVED transition")
    void shouldAllowTransitionFromOpenToResolved() {
        // Arrange
        Ticket openTicket = new Ticket(1L, "Open ticket", Priority.MEDIUM, TicketStatus.OPEN);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(openTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket result = ticketService.updateStatus(1L, TicketStatus.RESOLVED);

        // Assert
        assertEquals(TicketStatus.RESOLVED, result.getStatus());
    }

    @Test
    @DisplayName("Allows IN_PROGRESS to RESOLVED transition")
    void shouldAllowTransitionFromInProgressToResolved() {
        // Arrange
        Ticket inProgressTicket = new Ticket(1L, "In progress ticket", Priority.HIGH, TicketStatus.IN_PROGRESS);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(inProgressTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket result = ticketService.updateStatus(1L, TicketStatus.RESOLVED);

        // Assert
        assertEquals(TicketStatus.RESOLVED, result.getStatus());
    }

    @Test
    @DisplayName("Rejects any transition from RESOLVED status")
    void shouldRejectTransitionFromResolvedStatus() {
        // Arrange
        Ticket resolvedTicket = new Ticket(1L, "Resolved ticket", Priority.LOW, TicketStatus.RESOLVED);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(resolvedTicket));

        // Act & Assert
        assertThrows(
                InvalidStatusTransitionException.class,
                () -> ticketService.updateStatus(1L, TicketStatus.IN_PROGRESS)
        );
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Rejects forbidden IN_PROGRESS to OPEN transition")
    void shouldRejectForbiddenTransitionFromInProgressToOpen() {
        // Arrange
        Ticket inProgressTicket = new Ticket(1L, "In progress ticket", Priority.MEDIUM, TicketStatus.IN_PROGRESS);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(inProgressTicket));

        // Act & Assert
        assertThrows(
                InvalidStatusTransitionException.class,
                () -> ticketService.updateStatus(1L, TicketStatus.OPEN)
        );
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("Throws when updating status of unknown ticket")
    void shouldThrowWhenUpdatingStatusOfUnknownTicket() {
        // Arrange
        when(ticketRepository.findById(42L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.updateStatus(42L, TicketStatus.RESOLVED)
        );
        verify(ticketRepository, never()).save(any(Ticket.class));
    }
}
