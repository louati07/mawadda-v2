package com.mawadda.repository;

import com.mawadda.entity.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Integer> {
    List<Allergy> findByPatientId(Integer patientId);
}
