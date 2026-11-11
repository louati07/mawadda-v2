package com.mawadda.entity;

import com.mawadda.enums.Severity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "allergies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private String allergen;

    @Column(nullable = false)
    private String reaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @CreationTimestamp
    @Column(name = "logged_at", updatable = false)
    private LocalDateTime loggedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logged_by_id", nullable = false)
    private User loggedBy;
}
