package com.mawadda.service;

import com.mawadda.entity.Patient;

import java.util.List;

public interface IPatientService {
    Patient add(Patient patient);
    Patient modify(Integer id, Patient patient);
    Patient show(Integer id);
    List<Patient> showAll();
    List<Patient> showByService(Integer serviceId);
    List<Patient> showByParentCin(String cin);
    void delete(Integer id);
}
