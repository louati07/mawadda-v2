package com.mawadda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicationOrderRequest(
        @NotNull(message = "Admission ID is required")
        Integer admissionId,

        @NotBlank(message = "Medication name is required")
        String medication,

        @NotBlank(message = "Dosage is required")
        String dosage,

        @NotBlank(message = "Route is required")
        String route,

        @NotNull(message = "Ordered by User ID is required")
        Integer orderedById
) {}
