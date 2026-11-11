package com.mawadda.repository;

import com.mawadda.entity.SurgicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurgicalHistoryRepository extends JpaRepository<SurgicalHistory, Integer> {
    List<SurgicalHistory> findByPatientId(Integer patientId);
}
