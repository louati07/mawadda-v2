package com.mawadda.dto;

import jakarta.validation.constraints.NotBlank;

public record ParentRequest(
        @NotBlank(message = "CIN is required")
        String cin,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotBlank(message = "Phone number is required")
        String phone
) {}
