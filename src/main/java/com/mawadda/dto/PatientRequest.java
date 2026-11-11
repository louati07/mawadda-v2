package com.mawadda.dto;

import com.mawadda.enums.BirthType;
import com.mawadda.enums.BloodType;
import com.mawadda.enums.Sex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatientRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotNull(message = "Birthdate is required")
        LocalDate birthdate,

        @NotNull(message = "Sex is required")
        Sex sex,

        BloodType bloodType,

        @NotNull(message = "Birth type is required")
        BirthType birthType,

        @NotNull(message = "Birth weight is required")
        Double birthWeight,

        @NotNull(message = "Gestational age is required")
        Integer gestationalAge,

        String complications,

        @NotNull(message = "Parent ID is required")
        Integer parentId,

        @NotNull(message = "Service ID is required")
        Integer serviceId
) {}
