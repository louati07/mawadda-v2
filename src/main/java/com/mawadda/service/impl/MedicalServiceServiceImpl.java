package com.mawadda.service.impl;

import com.mawadda.entity.MedicalService;
import com.mawadda.exception.ResourceNotFoundException;
import com.mawadda.repository.MedicalServiceRepository;
import com.mawadda.service.IMedicalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalServiceServiceImpl implements IMedicalServiceService {

    private final MedicalServiceRepository serviceRepository;
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public MedicalService add(MedicalService service) {
        if (service.getInviteCode() == null || service.getInviteCode().isBlank()) {
            service.setInviteCode(generateUniqueInviteCode());
        }
        return serviceRepository.save(service);
    }

    @Override
    public MedicalService modify(Integer id, MedicalService service) {
        MedicalService existing = show(id);
        existing.setName(service.getName());
        return serviceRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalService show(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical Service not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalService showByInviteCode(String inviteCode) {
        return serviceRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new ResourceNotFoundException("Medical Service not found with invite code: " + inviteCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalService> showAll() {
        return serviceRepository.findAll();
    }

    @Override
    public void delete(Integer id) {
        MedicalService service = show(id);
        serviceRepository.delete(service);
    }

    @Override
    public String generateUniqueInviteCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
            }
            code = sb.toString();
        } while (serviceRepository.existsByInviteCode(code));
        return code;
    }
}
