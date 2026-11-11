package com.mawadda.dto;

import com.mawadda.entity.Patient;
import com.mawadda.enums.BirthType;
import com.mawadda.enums.BloodType;
import com.mawadda.enums.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResponse(
        Integer id,
        String firstName,
        String lastName,
        LocalDate birthdate,
        Sex sex,
        BloodType bloodType,
        BirthType birthType,
        Double birthWeight,
        Integer gestationalAge,
        String complications,
        Integer parentId,
        String parentName,
        String parentCin,
        Integer serviceId,
        String serviceName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PatientResponse fromEntity(Patient p) {
        if (p == null) return null;
        return new PatientResponse(
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getBirthdate(),
                p.getSex(),
                p.getBloodType(),
                p.getBirthType(),
                p.getBirthWeight(),
                p.getGestationalAge(),
                p.getComplications(),
                p.getParent() != null ? p.getParent().getId() : null,
                p.getParent() != null ? p.getParent().getFirstName() + " " + p.getParent().getLastName() : null,
                p.getParent() != null ? p.getParent().getCin() : null,
                p.getService() != null ? p.getService().getId() : null,
                p.getService() != null ? p.getService().getName() : null,
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
