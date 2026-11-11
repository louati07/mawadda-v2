package com.mawadda.repository;

import com.mawadda.entity.MedicationOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationOrderRepository extends JpaRepository<MedicationOrder, Integer> {
    List<MedicationOrder> findByAdmissionId(Integer admissionId);
}
