package com.mawadda.service;

import com.mawadda.entity.Allergy;

import java.util.List;

public interface IAllergyService {
    Allergy add(Allergy allergy);
    Allergy modify(Integer id, Allergy allergy);
    Allergy show(Integer id);
    List<Allergy> showAll();
    List<Allergy> showByPatient(Integer patientId);
    void delete(Integer id);
}
