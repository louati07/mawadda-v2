package com.mawadda.dto;

import com.mawadda.entity.Diagnosis;
import com.mawadda.enums.DiagType;

import java.time.LocalDateTime;

public record DiagnosisResponse(
        Integer id,
        Integer admissionId,
        String description,
        DiagType type,
        LocalDateTime loggedAt,
        Integer loggedById,
        String loggedByName
) {
    public static DiagnosisResponse fromEntity(Diagnosis d) {
        if (d == null) return null;
        return new DiagnosisResponse(
                d.getId(),
                d.getAdmission() != null ? d.getAdmission().getId() : null,
                d.getDescription(),
                d.getType(),
                d.getLoggedAt(),
                d.getLoggedBy() != null ? d.getLoggedBy().getId() : null,
                d.getLoggedBy() != null ? d.getLoggedBy().getFirstName() + " " + d.getLoggedBy().getLastName() : null
        );
    }
}
