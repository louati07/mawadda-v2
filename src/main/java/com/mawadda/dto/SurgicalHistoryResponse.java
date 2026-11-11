package com.mawadda.dto;

import com.mawadda.entity.SurgicalHistory;

import java.time.LocalDate;

public record SurgicalHistoryResponse(
        Integer id,
        Integer patientId,
        String patientName,
        String name,
        String bodySite,
        LocalDate date,
        String complications,
        String notes
) {
    public static SurgicalHistoryResponse fromEntity(SurgicalHistory s) {
        if (s == null) return null;
        return new SurgicalHistoryResponse(
                s.getId(),
                s.getPatient() != null ? s.getPatient().getId() : null,
                s.getPatient() != null ? s.getPatient().getFirstName() + " " + s.getPatient().getLastName() : null,
                s.getName(),
                s.getBodySite(),
                s.getDate(),
                s.getComplications(),
                s.getNotes()
        );
    }
}
