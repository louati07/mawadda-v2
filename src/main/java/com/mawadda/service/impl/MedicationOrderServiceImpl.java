package com.mawadda.service.impl;

import com.mawadda.entity.Admission;
import com.mawadda.entity.MedicationOrder;
import com.mawadda.entity.User;
import com.mawadda.exception.ConflictException;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.AdmissionRepository;
import com.mawadda.repository.MedicationOrderRepository;
import com.mawadda.repository.UserRepository;
import com.mawadda.service.IMedicationOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicationOrderServiceImpl implements IMedicationOrderService {

    private final MedicationOrderRepository medicationOrderRepository;
    private final AdmissionRepository admissionRepository;
    private final UserRepository userRepository;

    @Override
    public MedicationOrder add(MedicationOrder medicationOrder) {
        if (medicationOrder.getAdmission() != null && medicationOrder.getAdmission().getId() != null) {
            Admission admission = admissionRepository.findById(medicationOrder.getAdmission().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admission not found with ID: " + medicationOrder.getAdmission().getId()));
            medicationOrder.setAdmission(admission);
        }

        if (medicationOrder.getOrderedBy() != null && medicationOrder.getOrderedBy().getId() != null) {
            User user = userRepository.findById(medicationOrder.getOrderedBy().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + medicationOrder.getOrderedBy().getId()));
            medicationOrder.setOrderedBy(user);
        }

        return medicationOrderRepository.save(medicationOrder);
    }

    @Override
    public MedicationOrder modify(Integer id, MedicationOrder medicationOrder) {
        MedicationOrder existing = show(id);
        existing.setMedication(medicationOrder.getMedication());
        existing.setDosage(medicationOrder.getDosage());
        existing.setRoute(medicationOrder.getRoute());
        return medicationOrderRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicationOrder show(Integer id) {
        return medicationOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication Order not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationOrder> showAll() {
        return medicationOrderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationOrder> showByAdmission(Integer admissionId) {
        return medicationOrderRepository.findByAdmissionId(admissionId);
    }

    @Override
    public MedicationOrder discontinue(Integer id) {
        MedicationOrder existing = show(id);
        if (existing.getDiscontinuedAt() != null) {
            throw new ConflictException("Medication Order with ID " + id + " is already discontinued");
        }
        existing.setDiscontinuedAt(LocalDateTime.now());
        return medicationOrderRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        MedicationOrder medicationOrder = show(id);
        medicationOrderRepository.delete(medicationOrder);
    }
}
