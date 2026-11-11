package com.mawadda.service;

import com.mawadda.entity.MedicalService;

import java.util.List;

public interface IMedicalServiceService {
    MedicalService add(MedicalService service);
    MedicalService modify(Integer id, MedicalService service);
    MedicalService show(Integer id);
    MedicalService showByInviteCode(String inviteCode);
    List<MedicalService> showAll();
    void delete(Integer id);
    String generateUniqueInviteCode();
}
