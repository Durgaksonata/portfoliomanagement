package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.PursuitTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PursuitTrackerRepository extends JpaRepository<PursuitTracker, Integer> {
Optional<PursuitTracker> findByProjectorPursuit(String projectorPursuit);



    @Query("SELECT p FROM PursuitTracker p WHERE CONCAT(p.account, p.projectorPursuit) = :concatenatedName")
    PursuitTracker findByConcatenatedName(@Param("concatenatedName") String concatenatedName);

    List<PursuitTracker> findByDeliveryManager(String deliveryManager);


    // Custom query to find the maximum pursuitNo
    @Query("SELECT COALESCE(MAX(p.pursuitid), 0) FROM PursuitTracker p")
    int findMaxPursuitid();


    List<PursuitTracker> findByDeliveryManagerAndDeliveryDirectorAndAccountAndTypeAndTcvAndIdentifiedmonthAndPursuitstatusAndStageAndPursuitProbabilityAndProjectorPursuitAndPursuitorpotentialAndLikelyClosureorActualClosureAndRemarks(String deliveryManager, String deliveryDirector, String account, String type, float tcv, Date identifiedmonth, String pursuitstatus, String stage, int pursuitProbability, String projectorPursuit, String pursuitorpotential, Date likelyClosureorActualClosure, String remarks);


    List<PursuitTracker> findByPursuitid(int pursuitId);
}
