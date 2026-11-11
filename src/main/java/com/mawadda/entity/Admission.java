package com.mawadda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mawadda.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private MedicalService service;

    @CreationTimestamp
    @Column(name = "admission_date", updatable = false)
    private LocalDateTime admissionDate;

    // Safety lock: if null, patient is currently admitted in hospital bed
    @Column(name = "discharge_date")
    private LocalDateTime dischargeDate;

    @Column(name = "chief_complaint", nullable = false)
    private String chiefComplaint;

    @Column(nullable = false)
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PatientStatus status = PatientStatus.Stable;

    @JsonIgnore
    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PewsScore> pewsScores = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MedicationOrder> medications = new ArrayList<>();
}
