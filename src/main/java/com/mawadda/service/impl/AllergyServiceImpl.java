package com.mawadda.service.impl;

import com.mawadda.entity.Allergy;
import com.mawadda.entity.Patient;
import com.mawadda.entity.User;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.AllergyRepository;
import com.mawadda.repository.PatientRepository;
import com.mawadda.repository.UserRepository;
import com.mawadda.service.IAllergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AllergyServiceImpl implements IAllergyService {

    private final AllergyRepository allergyRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    public Allergy add(Allergy allergy) {
        if (allergy.getPatient() != null && allergy.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(allergy.getPatient().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + allergy.getPatient().getId()));
            allergy.setPatient(patient);
        }

        if (allergy.getLoggedBy() != null && allergy.getLoggedBy().getId() != null) {
            User user = userRepository.findById(allergy.getLoggedBy().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + allergy.getLoggedBy().getId()));
            allergy.setLoggedBy(user);
        }

        return allergyRepository.save(allergy);
    }

    @Override
    public Allergy modify(Integer id, Allergy allergy) {
        Allergy existing = show(id);
        existing.setAllergen(allergy.getAllergen());
        existing.setReaction(allergy.getReaction());
        existing.setSeverity(allergy.getSeverity());
        return allergyRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Allergy show(Integer id) {
        return allergyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Allergy> showAll() {
        return allergyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Allergy> showByPatient(Integer patientId) {
        return allergyRepository.findByPatientId(patientId);
    }

    @Override
    public void delete(Integer id) {
        Allergy allergy = show(id);
        allergyRepository.delete(allergy);
    }
}
