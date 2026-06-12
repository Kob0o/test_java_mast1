package com.example.exo11.dto;

import com.example.exo11.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "Status is required")
        TicketStatus status
) {
}
