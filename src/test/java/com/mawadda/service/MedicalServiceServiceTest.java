package com.mawadda.service;

import com.mawadda.entity.MedicalService;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.MedicalServiceRepository;
import com.mawadda.service.impl.MedicalServiceServiceImpl;
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
class MedicalServiceServiceTest {

    @Mock
    private MedicalServiceRepository serviceRepository;

    @InjectMocks
    private MedicalServiceServiceImpl medicalServiceService;

    private MedicalService service;

    @BeforeEach
    void setUp() {
        service = MedicalService.builder()
                .id(1)
                .name("Neonatology ICU")
                .build();
    }

    @Test
    @DisplayName("Should auto-generate invite code if not provided on add")
    void testAddMedicalServiceAutoGeneratesInviteCode() {
        when(serviceRepository.existsByInviteCode(anyString())).thenReturn(false);
        when(serviceRepository.save(any(MedicalService.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MedicalService saved = medicalServiceService.add(service);

        assertThat(saved.getInviteCode()).isNotNull();
        assertThat(saved.getInviteCode()).hasSize(6);
        assertThat(saved.getName()).isEqualTo("Neonatology ICU");
    }

    @Test
    @DisplayName("Should lookup medical service by invite code")
    void testShowByInviteCodeSuccess() {
        service.setInviteCode("NEO123");
        when(serviceRepository.findByInviteCode("NEO123")).thenReturn(Optional.of(service));

        MedicalService found = medicalServiceService.showByInviteCode("NEO123");

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Neonatology ICU");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for unknown invite code")
    void testShowByInviteCodeNotFound() {
        when(serviceRepository.findByInviteCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicalServiceService.showByInviteCode("UNKNOWN"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("invite code");
    }
}

