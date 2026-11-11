package com.mawadda.dto;

import com.mawadda.entity.Allergy;
import com.mawadda.enums.Severity;

import java.time.LocalDateTime;

public record AllergyResponse(
        Integer id,
        Integer patientId,
        String allergen,
        String reaction,
        Severity severity,
        LocalDateTime loggedAt,
        Integer loggedById,
        String loggedByName
) {
    public static AllergyResponse fromEntity(Allergy a) {
        if (a == null) return null;
        return new AllergyResponse(
                a.getId(),
                a.getPatient() != null ? a.getPatient().getId() : null,
                a.getAllergen(),
                a.getReaction(),
                a.getSeverity(),
                a.getLoggedAt(),
                a.getLoggedBy() != null ? a.getLoggedBy().getId() : null,
                a.getLoggedBy() != null ? a.getLoggedBy().getFirstName() + " " + a.getLoggedBy().getLastName() : null
        );
    }
}
