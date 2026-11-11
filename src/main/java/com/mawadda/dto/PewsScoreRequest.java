package com.mawadda.dto;

import jakarta.validation.constraints.NotNull;

public record PewsScoreRequest(
        @NotNull(message = "Admission ID is required")
        Integer admissionId,

        @NotNull(message = "Heart rate is required")
        Integer heartRate,

        @NotNull(message = "Respiratory rate is required")
        Integer respiratoryRate,

        @NotNull(message = "Oxygen saturation is required")
        Integer oxygenSaturation,

        @NotNull(message = "Temperature is required")
        Double temperature,

        @NotNull(message = "Capillary refill time is required")
        Integer capillaryRefill,

        @NotNull(message = "Consciousness state is required")
        String consciousness,

        Boolean needsOxygen,

        Integer totalScore,

        @NotNull(message = "Recorded by User ID is required")
        Integer recordedById
) {}
