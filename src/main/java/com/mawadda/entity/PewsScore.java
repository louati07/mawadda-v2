package com.mawadda.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pews_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PewsScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @CreationTimestamp
    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;

    // Raw vitals
    @Column(name = "heart_rate", nullable = false)
    private Integer heartRate;

    @Column(name = "respiratory_rate", nullable = false)
    private Integer respiratoryRate;

    @Column(name = "oxygen_saturation", nullable = false)
    private Integer oxygenSaturation;

    @Column(nullable = false)
    private Double temperature;

    @Column(name = "capillary_refill", nullable = false)
    private Integer capillaryRefill;

    @Column(nullable = false)
    private String consciousness;

    @Column(name = "needs_oxygen", nullable = false)
    @Builder.Default
    private Boolean needsOxygen = false;

    // Calculated score (0-9)
    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by_id", nullable = false)
    private User recordedBy;

    // Audit correction
    @Column(name = "is_error", nullable = false)
    @Builder.Default
    private Boolean isError = false;

    @Column(name = "error_reason")
    private String errorReason;
}
