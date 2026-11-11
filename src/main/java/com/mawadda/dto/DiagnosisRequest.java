package com.mawadda.dto;

import com.mawadda.enums.DiagType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DiagnosisRequest(
        @NotNull(message = "Admission ID is required")
        Integer admissionId,

        @NotBlank(message = "Diagnosis description is required")
        String description,

        @NotNull(message = "Diagnosis type is required")
        DiagType type,

        @NotNull(message = "Logged by User ID is required")
        Integer loggedById
) {}
