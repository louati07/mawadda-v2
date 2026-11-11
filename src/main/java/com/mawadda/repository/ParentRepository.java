package com.mawadda.repository;

import com.mawadda.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Integer> {
    Optional<Parent> findByCin(String cin);
    boolean existsByCin(String cin);
}
