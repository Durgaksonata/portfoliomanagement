package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pursuittracker")
public class PursuitTrackerController {
    @Autowired
    private PursuitTrackerRepository pursuitTrackerRepository;

    @PostMapping("/save")
    public PursuitTracker addPursuitTracker(@RequestBody PursuitTracker pursuitTracker) {
        return pursuitTrackerRepository.save(pursuitTracker);
    }


    @GetMapping("/get")
    public List<PursuitTracker> getAllPursuitTrackers() {
        return pursuitTrackerRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<PursuitTracker> getPursuitTrackerById(@PathVariable(value = "id") int pursuitTrackerId) {
        Optional<PursuitTracker> pursuitTracker = pursuitTrackerRepository.findById(pursuitTrackerId);
        if (pursuitTracker.isPresent()) {
            return ResponseEntity.ok().body(pursuitTracker.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PursuitTracker> updatePursuitTracker(@PathVariable(value = "id") int pursuitTrackerId,
                                                               @RequestBody PursuitTracker pursuitTrackerDetails) {
        Optional<PursuitTracker> optionalPursuitTracker = pursuitTrackerRepository.findById(pursuitTrackerId);
        if (optionalPursuitTracker.isPresent()) {
            PursuitTracker existingPursuitTracker = optionalPursuitTracker.get();
            existingPursuitTracker.setDeliveryManager(pursuitTrackerDetails.getDeliveryManager());
            existingPursuitTracker.setAccount(pursuitTrackerDetails.getAccount());
            existingPursuitTracker.setType(pursuitTrackerDetails.getType());
            existingPursuitTracker.setTcv(pursuitTrackerDetails.getTcv());
            existingPursuitTracker.setIdentifiedmonth(pursuitTrackerDetails.getIdentifiedmonth());
            existingPursuitTracker.setPursuitstatus(pursuitTrackerDetails.getPursuitstatus());
            existingPursuitTracker.setStage(pursuitTrackerDetails.getStage());
            existingPursuitTracker.setPursuitProbability(pursuitTrackerDetails.getPursuitProbability());
            existingPursuitTracker.setProjectorPursuit(pursuitTrackerDetails.getProjectorPursuit());
            existingPursuitTracker.setPursuitorpotential(pursuitTrackerDetails.getPursuitorpotential());
            existingPursuitTracker.setLikelyClosureorActualClosure(pursuitTrackerDetails.getLikelyClosureorActualClosure());
            existingPursuitTracker.setStatusorRemarks(pursuitTrackerDetails.getStatusorRemarks());
            existingPursuitTracker.setYear(pursuitTrackerDetails.getYear());

            PursuitTracker updatedPursuitTracker = pursuitTrackerRepository.save(existingPursuitTracker);
            return ResponseEntity.ok(updatedPursuitTracker);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/updateByProjectorPursuit/{projectorPursuit}")
    public ResponseEntity<PursuitTracker> updatePursuitTrackerByProjectOrPursuit(@PathVariable(value = "projectorPursuit") String projectorPursuit,
                                                                                 @RequestBody PursuitTracker pursuitTrackerDetails) {
        Optional<PursuitTracker> optionalPursuitTracker = pursuitTrackerRepository.findByProjectorPursuit(projectorPursuit);
        if (optionalPursuitTracker.isPresent()) {
            PursuitTracker existingPursuitTracker = optionalPursuitTracker.get();
            existingPursuitTracker.setDeliveryManager(pursuitTrackerDetails.getDeliveryManager());
            existingPursuitTracker.setAccount(pursuitTrackerDetails.getAccount());
            existingPursuitTracker.setType(pursuitTrackerDetails.getType());
            existingPursuitTracker.setTcv(pursuitTrackerDetails.getTcv());
            existingPursuitTracker.setIdentifiedmonth(pursuitTrackerDetails.getIdentifiedmonth());
            existingPursuitTracker.setPursuitstatus(pursuitTrackerDetails.getPursuitstatus());
            existingPursuitTracker.setStage(pursuitTrackerDetails.getStage());
            existingPursuitTracker.setPursuitProbability(pursuitTrackerDetails.getPursuitProbability());
            existingPursuitTracker.setProjectorPursuit(pursuitTrackerDetails.getProjectorPursuit());
            existingPursuitTracker.setPursuitorpotential(pursuitTrackerDetails.getPursuitorpotential());
            existingPursuitTracker.setLikelyClosureorActualClosure(pursuitTrackerDetails.getLikelyClosureorActualClosure());
            existingPursuitTracker.setStatusorRemarks(pursuitTrackerDetails.getStatusorRemarks());
            existingPursuitTracker.setYear(pursuitTrackerDetails.getYear());

            PursuitTracker updatedPursuitTracker = pursuitTrackerRepository.save(existingPursuitTracker);
            return ResponseEntity.ok(updatedPursuitTracker);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    @DeleteMapping("/deleteByProjectorPursuit/{projectorPursuit}")
    public ResponseEntity<Void> deletePursuitTrackerByProjectorPursuit(@PathVariable(value = "projectorPursuit") String projectorPursuit) {
        Optional<PursuitTracker> optionalPursuitTracker = pursuitTrackerRepository.findByProjectorPursuit(projectorPursuit);
        if (optionalPursuitTracker.isPresent()) {
            pursuitTrackerRepository.deleteByProjectorPursuit(projectorPursuit);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}




