package com.example.exercice13.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "Source account is required")
        String fromNumber,
        @NotBlank(message = "Target account is required")
        String toNumber,
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be strictly positive")
        BigDecimal amount
) {
}
