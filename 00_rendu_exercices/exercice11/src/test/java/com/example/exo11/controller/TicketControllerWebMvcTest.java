package com.example.exo11.controller;

import com.example.exo11.exception.InvalidStatusTransitionException;
import com.example.exo11.exception.TicketNotFoundException;
import com.example.exo11.model.Priority;
import com.example.exo11.model.Ticket;
import com.example.exo11.model.TicketStatus;
import com.example.exo11.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Test
    void shouldReturnCreated_whenPostBodyIsValid() throws Exception {
        when(ticketService.createTicket("Login bug", Priority.HIGH))
                .thenReturn(new Ticket(1L, "Login bug", Priority.HIGH, TicketStatus.OPEN));

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Login bug\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tickets/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Login bug"));

        verify(ticketService).createTicket("Login bug", Priority.HIGH);
    }

    @Test
    void shouldReturnBadRequest_whenPostBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"priority\":\"LOW\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(ticketService, never()).createTicket(any(), any());
    }

    @Test
    void shouldReturnOk_whenTicketExists() throws Exception {
        when(ticketService.getTicketById(1L))
                .thenReturn(new Ticket(1L, "API issue", Priority.MEDIUM, TicketStatus.OPEN));

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("API issue"));

        verify(ticketService).getTicketById(1L);
    }

    @Test
    void shouldReturnNotFound_whenTicketDoesNotExist() throws Exception {
        when(ticketService.getTicketById(99L)).thenThrow(new TicketNotFoundException(99L));

        mockMvc.perform(get("/api/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(ticketService).getTicketById(99L);
    }

    @Test
    void shouldReturnOk_whenStatusUpdateIsValid() throws Exception {
        when(ticketService.updateStatus(1L, TicketStatus.IN_PROGRESS))
                .thenReturn(new Ticket(1L, "Login bug", Priority.HIGH, TicketStatus.IN_PROGRESS));

        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(ticketService).updateStatus(1L, TicketStatus.IN_PROGRESS);
    }

    @Test
    void shouldReturnConflict_whenStatusTransitionIsInvalid() throws Exception {
        when(ticketService.updateStatus(1L, TicketStatus.IN_PROGRESS))
                .thenThrow(new InvalidStatusTransitionException(TicketStatus.RESOLVED, TicketStatus.IN_PROGRESS));

        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        verify(ticketService).updateStatus(1L, TicketStatus.IN_PROGRESS);
    }
}
