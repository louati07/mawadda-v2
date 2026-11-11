package com.mawadda.service;

import com.mawadda.entity.SurgicalHistory;

import java.util.List;

public interface ISurgicalHistoryService {
    SurgicalHistory add(SurgicalHistory surgicalHistory);
    SurgicalHistory modify(Integer id, SurgicalHistory surgicalHistory);
    SurgicalHistory show(Integer id);
    List<SurgicalHistory> showAll();
    List<SurgicalHistory> showByPatient(Integer patientId);
    void delete(Integer id);
}
