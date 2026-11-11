package com.mawadda.dto;

import com.mawadda.enums.PatientStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdmissionRequest(
        @NotNull(message = "Patient ID is required")
        Integer patientId,

        @NotNull(message = "Service ID is required")
        Integer serviceId,

        @NotBlank(message = "Chief complaint is required")
        String chiefComplaint,

        @NotNull(message = "Patient weight is required")
        Double weight,

        PatientStatus status
) {}
