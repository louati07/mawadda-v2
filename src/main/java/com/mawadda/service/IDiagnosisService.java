package com.mawadda.service;

import com.mawadda.entity.Diagnosis;

import java.util.List;

public interface IDiagnosisService {
    Diagnosis add(Diagnosis diagnosis);
    Diagnosis modify(Integer id, Diagnosis diagnosis);
    Diagnosis show(Integer id);
    List<Diagnosis> showAll();
    List<Diagnosis> showByAdmission(Integer admissionId);
    void delete(Integer id);
}
