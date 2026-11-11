package com.mawadda.service;

import com.mawadda.dto.PewsScoreResponse;
import com.mawadda.entity.Admission;
import com.mawadda.entity.PewsScore;
import com.mawadda.entity.User;
import com.mawadda.repository.AdmissionRepository;
import com.mawadda.repository.PewsScoreRepository;
import com.mawadda.repository.UserRepository;
import com.mawadda.service.impl.PewsScoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PewsScoreServiceTest {

    @Mock
    private PewsScoreRepository pewsScoreRepository;

    @Mock
    private AdmissionRepository admissionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PewsScoreServiceImpl pewsScoreService;

    private Admission admission;
    private User recordedBy;

    @BeforeEach
    void setUp() {
        admission = Admission.builder().id(1).build();
        recordedBy = User.builder().id(1).firstName("Amina").lastName("Mansour").build();
    }

    @Test
    @DisplayName("Should correctly calculate normal PEWS score (0) for stable child")
    void testNormalPewsScoreCalculation() {
        PewsScore pews = PewsScore.builder()
                .admission(admission)
                .recordedBy(recordedBy)
                .heartRate(90)
                .respiratoryRate(22)
                .oxygenSaturation(99)
                .temperature(37.0)
                .capillaryRefill(1)
                .consciousness("Alert and smiling")
                .needsOxygen(false)
                .build();

        when(admissionRepository.findById(1)).thenReturn(Optional.of(admission));
        when(userRepository.findById(1)).thenReturn(Optional.of(recordedBy));
        when(pewsScoreRepository.save(any(PewsScore.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PewsScore saved = pewsScoreService.add(pews);

        assertThat(saved.getTotalScore()).isNotNull();
        assertThat(saved.getTotalScore()).isEqualTo(0);

        PewsScoreResponse response = PewsScoreResponse.fromEntity(saved);
        assertThat(response.triageRisk()).contains("LOW_RISK (GREEN)");
    }

    @Test
    @DisplayName("Should detect critical triage risk (PEWS >= 5)")
    void testCriticalPewsScoreCalculation() {
        PewsScore criticalPews = PewsScore.builder()
                .admission(admission)
                .recordedBy(recordedBy)
                .heartRate(170)
                .respiratoryRate(55)
                .oxygenSaturation(88)
                .temperature(39.5)
                .capillaryRefill(4)
                .consciousness("Lethargic")
                .needsOxygen(true)
                .build();

        when(admissionRepository.findById(1)).thenReturn(Optional.of(admission));
        when(userRepository.findById(1)).thenReturn(Optional.of(recordedBy));
        when(pewsScoreRepository.save(any(PewsScore.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PewsScore saved = pewsScoreService.add(criticalPews);

        assertThat(saved.getTotalScore()).isGreaterThanOrEqualTo(5);
        PewsScoreResponse response = PewsScoreResponse.fromEntity(saved);
        assertThat(response.triageRisk()).contains("CRITICAL_RISK (RED)");
    }

    @Test
    @DisplayName("Should clamp individual scores and total score to max 9")
    void testClampPewsScore() {
        int total = pewsScoreService.calculatePewsScore(10, 10, 10);
        assertThat(total).isEqualTo(9);

        int minTotal = pewsScoreService.calculatePewsScore(-5, -2, -1);
        assertThat(minTotal).isEqualTo(0);
    }

    @Test
    @DisplayName("Should mark a score as recorded in error with explanation")
    void testMarkAsError() {
        PewsScore existing = PewsScore.builder()
                .id(10)
                .totalScore(3)
                .isError(false)
                .build();

        when(pewsScoreRepository.findById(10)).thenReturn(Optional.of(existing));
        when(pewsScoreRepository.save(any(PewsScore.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PewsScore result = pewsScoreService.markAsError(10, "Wrong patient chart entered");

        assertThat(result.getIsError()).isTrue();
        assertThat(result.getErrorReason()).isEqualTo("Wrong patient chart entered");
    }
}

