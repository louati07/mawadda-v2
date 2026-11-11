package com.mawadda.service.impl;

import com.mawadda.entity.MedicalService;
import com.mawadda.entity.Parent;
import com.mawadda.entity.Patient;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.MedicalServiceRepository;
import com.mawadda.repository.ParentRepository;
import com.mawadda.repository.PatientRepository;
import com.mawadda.service.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements IPatientService {

    private final PatientRepository patientRepository;
    private final ParentRepository parentRepository;
    private final MedicalServiceRepository serviceRepository;

    @Override
    public Patient add(Patient patient) {
        if (patient.getParent() != null && patient.getParent().getId() != null) {
            Parent parent = parentRepository.findById(patient.getParent().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent not found with ID: " + patient.getParent().getId()));
            patient.setParent(parent);
        }
        if (patient.getService() != null && patient.getService().getId() != null) {
            MedicalService service = serviceRepository.findById(patient.getService().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medical Service not found with ID: " + patient.getService().getId()));
            patient.setService(service);
        }
        return patientRepository.save(patient);
    }

    @Override
    public Patient modify(Integer id, Patient patient) {
        Patient existing = show(id);
        existing.setFirstName(patient.getFirstName());
        existing.setLastName(patient.getLastName());
        existing.setBirthdate(patient.getBirthdate());
        existing.setSex(patient.getSex());
        if (patient.getBloodType() != null) {
            existing.setBloodType(patient.getBloodType());
        }
        existing.setBirthType(patient.getBirthType());
        existing.setBirthWeight(patient.getBirthWeight());
        existing.setGestationalAge(patient.getGestationalAge());
        existing.setComplications(patient.getComplications());
        return patientRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Patient show(Integer id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> showAll() {
        return patientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> showByService(Integer serviceId) {
        return patientRepository.findByServiceId(serviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> showByParentCin(String cin) {
        return patientRepository.findByParentCin(cin);
    }

    @Override
    public void delete(Integer id) {
        Patient patient = show(id);
        patientRepository.delete(patient);
    }
}
