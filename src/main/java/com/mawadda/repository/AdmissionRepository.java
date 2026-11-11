package com.mawadda.repository;

import com.mawadda.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Integer> {
    Optional<Admission> findByPatientIdAndDischargeDateIsNull(Integer patientId);
    List<Admission> findByServiceIdAndDischargeDateIsNull(Integer serviceId);
    boolean existsByPatientIdAndDischargeDateIsNull(Integer patientId);
}
