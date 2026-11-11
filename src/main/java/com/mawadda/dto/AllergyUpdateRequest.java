package com.mawadda.dto;

import com.mawadda.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AllergyUpdateRequest(
        @NotBlank(message = "Allergen is required")
        String allergen,

        @NotBlank(message = "Reaction description is required")
        String reaction,

        @NotNull(message = "Severity level is required")
        Severity severity
) {}

