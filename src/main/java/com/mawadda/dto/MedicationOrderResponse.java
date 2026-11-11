package com.mawadda.dto;

import com.mawadda.entity.MedicationOrder;

import java.time.LocalDateTime;

public record MedicationOrderResponse(
        Integer id,
        Integer admissionId,
        String medication,
        String dosage,
        String route,
        LocalDateTime startedAt,
        LocalDateTime discontinuedAt,
        boolean isActive,
        Integer orderedById,
        String orderedByName
) {
    public static MedicationOrderResponse fromEntity(MedicationOrder m) {
        if (m == null) return null;
        return new MedicationOrderResponse(
                m.getId(),
                m.getAdmission() != null ? m.getAdmission().getId() : null,
                m.getMedication(),
                m.getDosage(),
                m.getRoute(),
                m.getStartedAt(),
                m.getDiscontinuedAt(),
                m.getDiscontinuedAt() == null,
                m.getOrderedBy() != null ? m.getOrderedBy().getId() : null,
                m.getOrderedBy() != null ? m.getOrderedBy().getFirstName() + " " + m.getOrderedBy().getLastName() : null
        );
    }
}
