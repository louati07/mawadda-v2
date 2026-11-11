package com.mawadda.dto;

import com.mawadda.entity.MedicalService;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;

public record MedicalServiceResponse(
        Integer id,
        String name,
        String inviteCode,
        LocalDateTime createdAt,
        int totalUsers,
        int totalPatients,
        int totalAdmissions
) {
    public static MedicalServiceResponse fromEntity(MedicalService s) {
        if (s == null) return null;
        int usersCount = (s.getUsers() != null && Hibernate.isInitialized(s.getUsers())) ? s.getUsers().size() : 0;
        int patientsCount = (s.getPatients() != null && Hibernate.isInitialized(s.getPatients())) ? s.getPatients().size() : 0;
        int admissionsCount = (s.getAdmissions() != null && Hibernate.isInitialized(s.getAdmissions())) ? s.getAdmissions().size() : 0;

        return new MedicalServiceResponse(
                s.getId(),
                s.getName(),
                s.getInviteCode(),
                s.getCreatedAt(),
                usersCount,
                patientsCount,
                admissionsCount
        );
    }
}
