package com.mawadda.service;

import com.mawadda.entity.MedicalService;
import com.mawadda.entity.Parent;
import com.mawadda.entity.Patient;
import com.mawadda.enums.BirthType;
import com.mawadda.enums.BloodType;
import com.mawadda.enums.Sex;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.MedicalServiceRepository;
import com.mawadda.repository.ParentRepository;
import com.mawadda.repository.PatientRepository;
import com.mawadda.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private MedicalServiceRepository serviceRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Parent parent;
    private MedicalService medicalService;
    private Patient patient;

    @BeforeEach
    void setUp() {
        parent = Parent.builder()
                .id(1)
                .cin("AB123456")
                .firstName("Fatima")
                .lastName("Zahra")
                .phone("+212600112233")
                .build();

        medicalService = MedicalService.builder()
                .id(1)
                .name("Pediatric Cardiology")
                .inviteCode("CARDIO12")
                .build();

        patient = Patient.builder()
                .id(100)
                .firstName("Youssef")
                .lastName("Zahra")
                .birthdate(LocalDate.of(2023, 5, 10))
                .sex(Sex.Male)
                .bloodType(BloodType.O_POS)
                .birthType(BirthType.Vaginal)
                .birthWeight(3.4)
                .gestationalAge(39)
                .parent(parent)
                .service(medicalService)
                .build();
    }

    @Test
    @DisplayName("Should successfully register a pediatric patient")
    void testAddPatientSuccess() {
        when(parentRepository.findById(1)).thenReturn(Optional.of(parent));
        when(serviceRepository.findById(1)).thenReturn(Optional.of(medicalService));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient saved = patientService.add(patient);

        assertThat(saved).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Youssef");
        assertThat(saved.getParent().getCin()).isEqualTo("AB123456");
        assertThat(saved.getService().getName()).isEqualTo("Pediatric Cardiology");
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when parent does not exist")
    void testAddPatientParentNotFound() {
        when(parentRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.add(patient))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Parent not found");

        verify(patientRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve patients by parent CIN")
    void testShowByParentCin() {
        when(patientRepository.findByParentCin("AB123456")).thenReturn(List.of(patient));

        List<Patient> results = patientService.showByParentCin("AB123456");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFirstName()).isEqualTo("Youssef");
    }

    @Test
    @DisplayName("Should retrieve patient by ID")
    void testShowByIdSuccess() {
        when(patientRepository.findById(100)).thenReturn(Optional.of(patient));

        Patient found = patientService.show(100);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(100);
    }
}

