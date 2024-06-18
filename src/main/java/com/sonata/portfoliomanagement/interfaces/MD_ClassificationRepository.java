package com.sonata.portfoliomanagement.interfaces;



import com.sonata.portfoliomanagement.model.MD_Classification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MD_ClassificationRepository extends JpaRepository<MD_Classification, Integer> {
    Optional<MD_Classification> findByClassification(String classification);
}