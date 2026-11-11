package com.mawadda.service;

import com.mawadda.entity.MedicationOrder;

import java.util.List;

public interface IMedicationOrderService {
    MedicationOrder add(MedicationOrder medicationOrder);
    MedicationOrder modify(Integer id, MedicationOrder medicationOrder);
    MedicationOrder show(Integer id);
    List<MedicationOrder> showAll();
    List<MedicationOrder> showByAdmission(Integer admissionId);
    MedicationOrder discontinue(Integer id);
    void delete(Integer id);
}
