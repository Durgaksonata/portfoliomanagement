package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_Vertical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MD_VerticalRepository extends JpaRepository<MD_Vertical, Integer> {
    Optional<MD_Vertical> findByVertical(String vertical);
    boolean existsByVertical(String vertical);
}