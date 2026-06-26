package com.example.exercice13.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank(message = "Account number is required")
        String number,
        @NotBlank(message = "Holder is required")
        String holder
) {
}
