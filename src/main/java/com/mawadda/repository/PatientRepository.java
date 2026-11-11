package com.mawadda.repository;

import com.mawadda.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    List<Patient> findByServiceId(Integer serviceId);
    List<Patient> findByParentCin(String cin);
}
