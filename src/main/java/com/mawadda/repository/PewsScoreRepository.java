package com.mawadda.repository;

import com.mawadda.entity.PewsScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PewsScoreRepository extends JpaRepository<PewsScore, Integer> {
    List<PewsScore> findByAdmissionIdOrderByRecordedAtDesc(Integer admissionId);
}
