package com.mawadda.dto;

import com.mawadda.entity.Admission;
import com.mawadda.enums.PatientStatus;

import java.time.LocalDateTime;

public record AdmissionResponse(
        Integer id,
        Integer patientId,
        String patientName,
        Integer serviceId,
        String serviceName,
        LocalDateTime admissionDate,
        LocalDateTime dischargeDate,
        String chiefComplaint,
        Double weight,
        PatientStatus status,
        boolean isActive
) {
    public static AdmissionResponse fromEntity(Admission a) {
        if (a == null) return null;
        return new AdmissionResponse(
                a.getId(),
                a.getPatient() != null ? a.getPatient().getId() : null,
                a.getPatient() != null ? a.getPatient().getFirstName() + " " + a.getPatient().getLastName() : null,
                a.getService() != null ? a.getService().getId() : null,
                a.getService() != null ? a.getService().getName() : null,
                a.getAdmissionDate(),
                a.getDischargeDate(),
                a.getChiefComplaint(),
                a.getWeight(),
                a.getStatus(),
                a.getDischargeDate() == null
        );
    }
}
