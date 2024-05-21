package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.PursuitTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PursuitTrackerRepository extends JpaRepository<PursuitTracker, Integer> {
Optional<PursuitTracker> findByProjectorPursuit(String projectorPursuit);

    void deleteByProjectorPursuit(String projectorPursuit);


    @Query("SELECT p FROM PursuitTracker p WHERE CONCAT(p.account, p.projectorPursuit) = :concatenatedName")
    PursuitTracker findByConcatenatedName(@Param("concatenatedName") String concatenatedName);
}