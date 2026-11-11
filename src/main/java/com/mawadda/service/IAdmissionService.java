package com.mawadda.service;

import com.mawadda.entity.Admission;

import java.util.List;

public interface IAdmissionService {
    Admission add(Admission admission);
    Admission modify(Integer id, Admission admission);
    Admission show(Integer id);
    Admission showActiveAdmissionByPatient(Integer patientId);
    List<Admission> showAll();
    List<Admission> showActiveByService(Integer serviceId);
    Admission discharge(Integer id);
    void delete(Integer id);
}
