package com.mawadda.repository;

import com.mawadda.entity.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalServiceRepository extends JpaRepository<MedicalService, Integer> {
    Optional<MedicalService> findByInviteCode(String inviteCode);
    boolean existsByInviteCode(String inviteCode);
}
