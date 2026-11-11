package com.mawadda.dto;

import com.mawadda.entity.PewsScore;

import java.time.LocalDateTime;

public record PewsScoreResponse(
        Integer id,
        Integer admissionId,
        LocalDateTime recordedAt,
        Integer heartRate,
        Integer respiratoryRate,
        Integer oxygenSaturation,
        Double temperature,
        Integer capillaryRefill,
        String consciousness,
        Boolean needsOxygen,
        Integer totalScore,
        String triageRisk,
        Integer recordedById,
        String recordedByName,
        Boolean isError,
        String errorReason
) {
    public static PewsScoreResponse fromEntity(PewsScore p) {
        if (p == null) return null;
        int score = p.getTotalScore() != null ? p.getTotalScore() : 0;
        String risk = score <= 2 ? "LOW_RISK (GREEN)" : score <= 4 ? "MEDIUM_RISK (AMBER)" : "CRITICAL_RISK (RED)";

        return new PewsScoreResponse(
                p.getId(),
                p.getAdmission() != null ? p.getAdmission().getId() : null,
                p.getRecordedAt(),
                p.getHeartRate(),
                p.getRespiratoryRate(),
                p.getOxygenSaturation(),
                p.getTemperature(),
                p.getCapillaryRefill(),
                p.getConsciousness(),
                p.getNeedsOxygen(),
                p.getTotalScore(),
                risk,
                p.getRecordedBy() != null ? p.getRecordedBy().getId() : null,
                p.getRecordedBy() != null ? p.getRecordedBy().getFirstName() + " " + p.getRecordedBy().getLastName() : null,
                p.getIsError(),
                p.getErrorReason()
        );
    }
}
