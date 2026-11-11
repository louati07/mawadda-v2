package com.mawadda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SurgicalHistoryRequest(
        @NotNull(message = "Patient ID is required")
        Integer patientId,

        @NotBlank(message = "Procedure name is required")
        String name,

        @NotBlank(message = "Body site is required")
        String bodySite,

        @NotNull(message = "Surgery date is required")
        LocalDate date,

        String complications,

        String notes
) {}
