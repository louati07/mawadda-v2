package com.mawadda.dto;

import com.mawadda.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AllergyRequest(
        @NotNull(message = "Patient ID is required")
        Integer patientId,

        @NotBlank(message = "Allergen is required")
        String allergen,

        @NotBlank(message = "Reaction description is required")
        String reaction,

        @NotNull(message = "Severity level is required")
        Severity severity,

        @NotNull(message = "Logged by User ID is required")
        Integer loggedById
) {}
