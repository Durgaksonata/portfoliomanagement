package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_PursuitProbability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MD_PursuitProbabilityRepository extends JpaRepository<MD_PursuitProbability, Integer> {

    Optional<MD_PursuitProbability> findByPursuitStatusAndType(String pursuitStatus, String type);

}