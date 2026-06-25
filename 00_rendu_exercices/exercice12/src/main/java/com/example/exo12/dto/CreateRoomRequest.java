package com.example.exo12.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateRoomRequest(
        @NotBlank(message = "Name is required")
        String name,
        @Min(value = 1, message = "Capacity must be at least 1")
        int capacity
) {
}
