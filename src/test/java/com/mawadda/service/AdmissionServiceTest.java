package com.mawadda.service;

import com.mawadda.entity.Admission;
import com.mawadda.entity.MedicalService;
import com.mawadda.entity.Patient;
import com.mawadda.enums.PatientStatus;
import com.mawadda.exception.ConflictException;
import com.mawadda.repository.AdmissionRepository;
import com.mawadda.repository.MedicalServiceRepository;
import com.mawadda.repository.PatientRepository;
import com.mawadda.service.impl.AdmissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdmissionServiceTest {

    @Mock
    private AdmissionRepository admissionRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicalServiceRepository serviceRepository;

    @InjectMocks
    private AdmissionServiceImpl admissionService;

    private Patient patient;
    private MedicalService service;
    private Admission admission;

    @BeforeEach
    void setUp() {
        patient = Patient.builder().id(1).firstName("Sami").lastName("Benali").build();
        service = MedicalService.builder().id(2).name("Pediatric Surgery").build();
        admission = Admission.builder()
                .id(10)
                .patient(patient)
                .service(service)
                .chiefComplaint("Acute appendicitis")
                .weight(18.5)
                .status(PatientStatus.Stable)
                .build();
    }

    @Test
    @DisplayName("Should successfully admit a patient when no active admission exists")
    void testAddAdmissionSuccess() {
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(serviceRepository.findById(2)).thenReturn(Optional.of(service));
        when(admissionRepository.existsByPatientIdAndDischargeDateIsNull(1)).thenReturn(false);
        when(admissionRepository.save(any(Admission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Admission saved = admissionService.add(admission);

        assertThat(saved).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(PatientStatus.Stable);
        assertThat(saved.getChiefComplaint()).isEqualTo("Acute appendicitis");
        verify(admissionRepository, times(1)).save(admission);
    }

    @Test
    @DisplayName("Should throw ConflictException when patient is already actively admitted")
    void testAddAdmissionConflictAlreadyAdmitted() {
        when(admissionRepository.existsByPatientIdAndDischargeDateIsNull(1)).thenReturn(true);

        assertThatThrownBy(() -> admissionService.add(admission))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already has an active hospital admission");

        verify(admissionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should discharge an active admission and set dischargeDate timestamp")
    void testDischargeSuccess() {
        when(admissionRepository.findById(10)).thenReturn(Optional.of(admission));
        when(admissionRepository.save(any(Admission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Admission discharged = admissionService.discharge(10);

        assertThat(discharged.getStatus()).isEqualTo(PatientStatus.Discharged);
        assertThat(discharged.getDischargeDate()).isNotNull();
    }

    @Test
    @DisplayName("Should throw ConflictException when discharging already discharged patient")
    void testDischargeAlreadyDischarged() {
        admission.setDischargeDate(java.time.LocalDateTime.now());
        when(admissionRepository.findById(10)).thenReturn(Optional.of(admission));

        assertThatThrownBy(() -> admissionService.discharge(10))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already discharged");

        verify(admissionRepository, never()).save(any());
    }
}
