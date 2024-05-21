package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_PursuitProbabilityRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitProbability;
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

    @Autowired
    private MD_PursuitProbabilityRepository mdPursuitProbabilityRepository;


    @PostMapping("/save")
    public ResponseEntity<PursuitTracker> addPursuitTracker(@RequestBody PursuitTracker pursuitTracker) {
        // Calculate stage based on PursuitStatus and Type
        String stage = calculateStage(pursuitTracker.getPursuitstatus(), pursuitTracker.getType());
        pursuitTracker.setStage(stage);

        // Calculate pursuitProbability based on PursuitStatus and Type
        int pursuitProbability = calculatePursuitProbability(pursuitTracker.getPursuitstatus(), pursuitTracker.getType());
        pursuitTracker.setPursuitProbability(pursuitProbability);

        PursuitTracker savedPursuitTracker = pursuitTrackerRepository.save(pursuitTracker);
        return ResponseEntity.ok(savedPursuitTracker);
    }

    // Method to calculate stage based on PursuitStatus and Type
    private String calculateStage(String pursuitStatus, String type) {
        if (pursuitStatus == null || pursuitStatus.isEmpty() || type == null || type.isEmpty()) {
            return "";
        }

        Optional<MD_PursuitProbability> result = mdPursuitProbabilityRepository.findByPursuitStatusAndType(pursuitStatus, type);
        return result.map(MD_PursuitProbability::getStage).orElse("");
    }

    // Method to calculate pursuitProbability based on PursuitStatus and Type
    private int calculatePursuitProbability(String pursuitStatus, String type) {
        if (pursuitStatus == null || pursuitStatus.isEmpty() || type == null || type.isEmpty()) {
            return 0;
        }

        Optional<MD_PursuitProbability> result = mdPursuitProbabilityRepository.findByPursuitStatusAndType(pursuitStatus, type);
        return result.map(MD_PursuitProbability::getProbability).orElse(0);
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
            existingPursuitTracker.setRemarks(pursuitTrackerDetails.getRemarks());
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
            existingPursuitTracker.setRemarks(pursuitTrackerDetails.getRemarks());
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




