package com.mawadda.dto;

import jakarta.validation.constraints.NotBlank;

public record MedicalServiceRequest(
        @NotBlank(message = "Service name is required")
        String name,

        String inviteCode
) {}
