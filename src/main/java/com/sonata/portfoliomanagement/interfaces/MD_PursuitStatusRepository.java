package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_PursuitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MD_PursuitStatusRepository extends JpaRepository<MD_PursuitStatus, Integer> {
    Optional<MD_PursuitStatus> findByPursuitStatus(String pursuitStatus);
}

