package com.mawadda.dto;

import jakarta.validation.constraints.NotBlank;

public record MedicationOrderUpdateRequest(
        @NotBlank(message = "Medication name is required")
        String medication,

        @NotBlank(message = "Dosage is required")
        String dosage,

        @NotBlank(message = "Route is required")
        String route
) {}

