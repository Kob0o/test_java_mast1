package com.example.exo11.dto;

import com.example.exo11.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 3, message = "Title must be at least 3 characters")
        String title,
        @NotNull(message = "Priority is required")
        Priority priority
) {
}
