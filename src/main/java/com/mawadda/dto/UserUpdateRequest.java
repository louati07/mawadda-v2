package com.mawadda.dto;

import com.mawadda.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Size(min = 6, message = "Password must have at least 6 characters")
        String password,

        @NotNull(message = "Role is required")
        Role role,

        Integer serviceId
) {}

