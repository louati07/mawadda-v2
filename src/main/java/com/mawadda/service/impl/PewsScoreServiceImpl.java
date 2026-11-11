package com.mawadda.service.impl;

import com.mawadda.entity.Admission;
import com.mawadda.entity.PewsScore;
import com.mawadda.entity.User;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.AdmissionRepository;
import com.mawadda.repository.PewsScoreRepository;
import com.mawadda.repository.UserRepository;
import com.mawadda.service.IPewsScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PewsScoreServiceImpl implements IPewsScoreService {

    private final PewsScoreRepository pewsScoreRepository;
    private final AdmissionRepository admissionRepository;
    private final UserRepository userRepository;

    @Override
    public PewsScore add(PewsScore pewsScore) {
        if (pewsScore.getAdmission() != null && pewsScore.getAdmission().getId() != null) {
            Admission admission = admissionRepository.findById(pewsScore.getAdmission().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admission not found with ID: " + pewsScore.getAdmission().getId()));
            pewsScore.setAdmission(admission);
        }

        if (pewsScore.getRecordedBy() != null && pewsScore.getRecordedBy().getId() != null) {
            User user = userRepository.findById(pewsScore.getRecordedBy().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + pewsScore.getRecordedBy().getId()));
            pewsScore.setRecordedBy(user);
        }

        if (pewsScore.getTotalScore() == null) {
            int behavior = calculateBehaviorScore(pewsScore.getConsciousness());
            int cardio = calculateCardioScore(pewsScore.getCapillaryRefill(), pewsScore.getHeartRate());
            int resp = calculateRespiratoryScore(pewsScore.getRespiratoryRate(), pewsScore.getOxygenSaturation(), pewsScore.getNeedsOxygen());
            pewsScore.setTotalScore(calculatePewsScore(behavior, cardio, resp));
        }
        return pewsScoreRepository.save(pewsScore);
    }

    @Override
    public PewsScore modify(Integer id, PewsScore pewsScore) {
        PewsScore existing = show(id);
        existing.setHeartRate(pewsScore.getHeartRate());
        existing.setRespiratoryRate(pewsScore.getRespiratoryRate());
        existing.setOxygenSaturation(pewsScore.getOxygenSaturation());
        existing.setTemperature(pewsScore.getTemperature());
        existing.setCapillaryRefill(pewsScore.getCapillaryRefill());
        existing.setConsciousness(pewsScore.getConsciousness());
        existing.setNeedsOxygen(pewsScore.getNeedsOxygen());
        existing.setTotalScore(pewsScore.getTotalScore());
        return pewsScoreRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public PewsScore show(Integer id) {
        return pewsScoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PEWS Score not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PewsScore> showAll() {
        return pewsScoreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PewsScore> showByAdmission(Integer admissionId) {
        return pewsScoreRepository.findByAdmissionIdOrderByRecordedAtDesc(admissionId);
    }

    @Override
    public PewsScore markAsError(Integer id, String reason) {
        PewsScore existing = show(id);
        existing.setIsError(true);
        existing.setErrorReason(reason);
        return pewsScoreRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        PewsScore pewsScore = show(id);
        pewsScoreRepository.delete(pewsScore);
    }

    @Override
    public int calculatePewsScore(int behavior, int cardio, int resp) {
        int b = Math.max(0, Math.min(3, behavior));
        int c = Math.max(0, Math.min(3, cardio));
        int r = Math.max(0, Math.min(3, resp));
        return Math.min(9, b + c + r);
    }

    private int calculateBehaviorScore(String consciousness) {
        if (consciousness == null) return 0;
        String c = consciousness.trim().toLowerCase();
        if (c.contains("alert") || c.contains("normal") || c.contains("active") || c.contains("play")) {
            return 0;
        } else if (c.contains("sleep") || c.contains("irritable") || c.contains("consol")) {
            return 1;
        } else if (c.contains("lethargic") || c.contains("inconsolable")) {
            return 2;
        } else if (c.contains("comatos") || c.contains("pain") || c.contains("unresponsive") || c.contains("flaccid") || c.contains("unconscious")) {
            return 3;
        }
        return 0;
    }

    private int calculateCardioScore(Integer capillaryRefill, Integer heartRate) {
        int score = 0;
        if (capillaryRefill != null) {
            if (capillaryRefill <= 2) score = 0;
            else if (capillaryRefill == 3) score = 1;
            else if (capillaryRefill == 4) score = 2;
            else score = 3;
        }
        if (heartRate != null) {
            if (heartRate > 160 || heartRate < 60) score = Math.max(score, 3);
            else if (heartRate > 140) score = Math.max(score, 2);
            else if (heartRate > 120) score = Math.max(score, 1);
        }
        return Math.min(score, 3);
    }

    private int calculateRespiratoryScore(Integer respiratoryRate, Integer oxygenSaturation, Boolean needsOxygen) {
        int score = 0;
        if (Boolean.TRUE.equals(needsOxygen)) {
            score = 2;
        }
        if (oxygenSaturation != null) {
            if (oxygenSaturation < 90) score = Math.max(score, 3);
            else if (oxygenSaturation < 94) score = Math.max(score, 2);
            else if (oxygenSaturation < 96) score = Math.max(score, 1);
        }
        if (respiratoryRate != null) {
            if (respiratoryRate > 50) score = Math.max(score, 3);
            else if (respiratoryRate > 40) score = Math.max(score, 2);
            else if (respiratoryRate > 30) score = Math.max(score, 1);
        }
        return Math.min(score, 3);
    }
}
