package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.model.PursuitTracker;
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

    @PostMapping("/add")
    public PursuitTracker addPursuitTracker(@RequestBody PursuitTracker pursuitTracker) {
        return pursuitTrackerRepository.save(pursuitTracker);
    }

    @GetMapping("/getAll")
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
            existingPursuitTracker.setIdentified_month(pursuitTrackerDetails.getIdentified_month());
            existingPursuitTracker.setPursuit_status(pursuitTrackerDetails.getPursuit_status());
            existingPursuitTracker.setStage(pursuitTrackerDetails.getStage());
            existingPursuitTracker.setPursuit_probability(pursuitTrackerDetails.getPursuit_probability());
            existingPursuitTracker.setProject_or_pursuit(pursuitTrackerDetails.getProject_or_pursuit());
            existingPursuitTracker.setPursuit_or_potential(pursuitTrackerDetails.getPursuit_or_potential());
            existingPursuitTracker.setLikely_closure_or_actual_closure(pursuitTrackerDetails.getLikely_closure_or_actual_closure());
            existingPursuitTracker.setStatus_or_remarks(pursuitTrackerDetails.getStatus_or_remarks());
            existingPursuitTracker.setYear(pursuitTrackerDetails.getYear());

            PursuitTracker updatedPursuitTracker = pursuitTrackerRepository.save(existingPursuitTracker);
            return ResponseEntity.ok(updatedPursuitTracker);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    }



