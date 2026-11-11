package com.mawadda.service.impl;

import com.mawadda.entity.Admission;
import com.mawadda.entity.Diagnosis;
import com.mawadda.entity.User;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.AdmissionRepository;
import com.mawadda.repository.DiagnosisRepository;
import com.mawadda.repository.UserRepository;
import com.mawadda.service.IDiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiagnosisServiceImpl implements IDiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final AdmissionRepository admissionRepository;
    private final UserRepository userRepository;

    @Override
    public Diagnosis add(Diagnosis diagnosis) {
        if (diagnosis.getAdmission() != null && diagnosis.getAdmission().getId() != null) {
            Admission admission = admissionRepository.findById(diagnosis.getAdmission().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admission not found with ID: " + diagnosis.getAdmission().getId()));
            diagnosis.setAdmission(admission);
        }

        if (diagnosis.getLoggedBy() != null && diagnosis.getLoggedBy().getId() != null) {
            User user = userRepository.findById(diagnosis.getLoggedBy().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + diagnosis.getLoggedBy().getId()));
            diagnosis.setLoggedBy(user);
        }

        return diagnosisRepository.save(diagnosis);
    }

    @Override
    public Diagnosis modify(Integer id, Diagnosis diagnosis) {
        Diagnosis existing = show(id);
        existing.setDescription(diagnosis.getDescription());
        existing.setType(diagnosis.getType());
        return diagnosisRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Diagnosis show(Integer id) {
        return diagnosisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Diagnosis> showAll() {
        return diagnosisRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Diagnosis> showByAdmission(Integer admissionId) {
        return diagnosisRepository.findByAdmissionId(admissionId);
    }

    @Override
    public void delete(Integer id) {
        Diagnosis diagnosis = show(id);
        diagnosisRepository.delete(diagnosis);
    }
}
