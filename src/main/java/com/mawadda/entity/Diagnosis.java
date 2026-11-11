package com.mawadda.entity;

import com.mawadda.enums.DiagType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "diagnoses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiagType type;

    @CreationTimestamp
    @Column(name = "logged_at", updatable = false)
    private LocalDateTime loggedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logged_by_id", nullable = false)
    private User loggedBy;
}
