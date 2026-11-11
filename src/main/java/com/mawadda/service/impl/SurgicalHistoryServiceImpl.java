package com.mawadda.service.impl;

import com.mawadda.entity.Patient;
import com.mawadda.entity.SurgicalHistory;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.PatientRepository;
import com.mawadda.repository.SurgicalHistoryRepository;
import com.mawadda.service.ISurgicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SurgicalHistoryServiceImpl implements ISurgicalHistoryService {

    private final SurgicalHistoryRepository surgicalHistoryRepository;
    private final PatientRepository patientRepository;

    @Override
    public SurgicalHistory add(SurgicalHistory surgicalHistory) {
        if (surgicalHistory.getPatient() != null && surgicalHistory.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(surgicalHistory.getPatient().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + surgicalHistory.getPatient().getId()));
            surgicalHistory.setPatient(patient);
        }
        return surgicalHistoryRepository.save(surgicalHistory);
    }

    @Override
    public SurgicalHistory modify(Integer id, SurgicalHistory surgicalHistory) {
        SurgicalHistory existing = show(id);
        existing.setName(surgicalHistory.getName());
        existing.setBodySite(surgicalHistory.getBodySite());
        existing.setDate(surgicalHistory.getDate());
        existing.setComplications(surgicalHistory.getComplications());
        existing.setNotes(surgicalHistory.getNotes());
        return surgicalHistoryRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SurgicalHistory show(Integer id) {
        return surgicalHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Surgical history not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurgicalHistory> showAll() {
        return surgicalHistoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SurgicalHistory> showByPatient(Integer patientId) {
        return surgicalHistoryRepository.findByPatientId(patientId);
    }

    @Override
    public void delete(Integer id) {
        SurgicalHistory surgicalHistory = show(id);
        surgicalHistoryRepository.delete(surgicalHistory);
    }
}
