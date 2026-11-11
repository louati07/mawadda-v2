package com.mawadda.service.impl;

import com.mawadda.entity.Admission;
import com.mawadda.entity.MedicalService;
import com.mawadda.entity.Patient;
import com.mawadda.enums.PatientStatus;
import com.mawadda.exception.ConflictException;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.AdmissionRepository;
import com.mawadda.repository.MedicalServiceRepository;
import com.mawadda.repository.PatientRepository;
import com.mawadda.service.IAdmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdmissionServiceImpl implements IAdmissionService {

    private final AdmissionRepository admissionRepository;
    private final PatientRepository patientRepository;
    private final MedicalServiceRepository serviceRepository;

    @Override
    public Admission add(Admission admission) {
        Integer patientId = admission.getPatient().getId();
        if (admissionRepository.existsByPatientIdAndDischargeDateIsNull(patientId)) {
            throw new ConflictException("Patient with ID " + patientId + " already has an active hospital admission");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));
        admission.setPatient(patient);

        if (admission.getService() != null && admission.getService().getId() != null) {
            MedicalService service = serviceRepository.findById(admission.getService().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medical Service not found with ID: " + admission.getService().getId()));
            admission.setService(service);
        }

        admission.setStatus(admission.getStatus() != null ? admission.getStatus() : PatientStatus.Stable);
        return admissionRepository.save(admission);
    }

    @Override
    public Admission modify(Integer id, Admission admission) {
        Admission existing = show(id);
        existing.setChiefComplaint(admission.getChiefComplaint());
        existing.setWeight(admission.getWeight());
        if (admission.getStatus() != null) {
            existing.setStatus(admission.getStatus());
        }
        return admissionRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Admission show(Integer id) {
        return admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Admission showActiveAdmissionByPatient(Integer patientId) {
        return admissionRepository.findByPatientIdAndDischargeDateIsNull(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("No active admission found for patient ID: " + patientId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Admission> showAll() {
        return admissionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Admission> showActiveByService(Integer serviceId) {
        return admissionRepository.findByServiceIdAndDischargeDateIsNull(serviceId);
    }

    @Override
    public Admission discharge(Integer id) {
        Admission existing = show(id);
        if (existing.getDischargeDate() != null) {
            throw new ConflictException("Admission with ID " + id + " is already discharged");
        }
        existing.setDischargeDate(LocalDateTime.now());
        existing.setStatus(PatientStatus.Discharged);
        return admissionRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        Admission admission = show(id);
        admissionRepository.delete(admission);
    }
}
