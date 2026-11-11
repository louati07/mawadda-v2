package com.mawadda.dto;

import com.mawadda.enums.PatientStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdmissionUpdateRequest(
        @NotBlank(message = "Chief complaint is required")
        String chiefComplaint,

        @NotNull(message = "Weight is required")
        Double weight,

        @NotNull(message = "Status is required")
        PatientStatus status
) {}

