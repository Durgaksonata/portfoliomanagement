package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_PursuitProbabilityRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitActionsRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitProbability;
import com.sonata.portfoliomanagement.model.PursuitActions;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import com.sonata.portfoliomanagement.model.PursuitTrackerDTO;
import com.sonata.portfoliomanagement.services.PursuitTrackerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/pursuittracker")
public class PursuitTrackerController {
    @Autowired
    private PursuitTrackerRepository pursuitTrackerRepository;
    @Autowired
    private PursuitActionsRepository pursuitActionsRepository;



    @Autowired
    private MD_PursuitProbabilityRepository mdPursuitProbabilityRepository;

    @PostMapping("/save")
    public ResponseEntity<?> addPursuitTrackers(@Valid @RequestBody List<PursuitTracker> pursuitTrackers) {
        List<PursuitTracker> savedPursuitTrackers = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        List<String> duplicateProjectorPursuits = new ArrayList<>();

        for (PursuitTracker pursuitTracker : pursuitTrackers) {
            StringJoiner emptyFields = new StringJoiner(", ");

            if (pursuitTracker.getProjectorPursuit().isEmpty()) {
                emptyFields.add("Projector Pursuit");
            }

            if (pursuitTracker.getPursuitstatus().isEmpty()) {
                emptyFields.add("Pursuit Status");
            }

            if (pursuitTracker.getType().isEmpty()) {
                emptyFields.add("Type");
            }

            if (emptyFields.length() > 0) {
                String message = "Required fields cannot be empty: " + emptyFields.toString();
                response.put("message", message);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Calculate stage based on PursuitStatus and Type
            String stage = calculateStage(pursuitTracker.getPursuitstatus(), pursuitTracker.getType());
            pursuitTracker.setStage(stage);

            // Calculate pursuitProbability based on PursuitStatus and Type
            int pursuitProbability = calculatePursuitProbability(pursuitTracker.getPursuitstatus(), pursuitTracker.getType());
            pursuitTracker.setPursuitProbability(pursuitProbability);

            // Check for duplicate entry based on all fields
            List<PursuitTracker> existingEntries = pursuitTrackerRepository.findByDeliveryManagerAndDeliveryDirectorAndAccountAndTypeAndTcvAndIdentifiedmonthAndPursuitstatusAndStageAndPursuitProbabilityAndProjectorPursuitAndPursuitorpotentialAndLikelyClosureorActualClosureAndRemarks(
                    pursuitTracker.getDeliveryManager(),
                    pursuitTracker.getDeliveryDirector(),
                    pursuitTracker.getAccount(),
                    pursuitTracker.getType(),
                    pursuitTracker.getTcv(),
                    pursuitTracker.getIdentifiedmonth(),
                    pursuitTracker.getPursuitstatus(),
                    pursuitTracker.getStage(),
                    pursuitTracker.getPursuitProbability(),
                    pursuitTracker.getProjectorPursuit(),
                    pursuitTracker.getPursuitorpotential(),
                    pursuitTracker.getLikelyClosureorActualClosure(),
                    pursuitTracker.getRemarks()
            );

            if (!existingEntries.isEmpty()) {
                duplicateProjectorPursuits.add(pursuitTracker.getProjectorPursuit());
                continue; // Skip this entry if it's a duplicate
            }

            // Check if the projectorPursuit already exists in the repository
            Optional<PursuitTracker> existingPursuitTracker = pursuitTrackerRepository.findByProjectorPursuit(pursuitTracker.getProjectorPursuit());
            if (existingPursuitTracker.isPresent()) {
                duplicateProjectorPursuits.add(pursuitTracker.getProjectorPursuit());
                continue; // Skip this entry if projectorPursuit is a duplicate
            } else {
                // Generate a new pursuitId if projectorPursuit doesn't exist
                int maxPursuitid = pursuitTrackerRepository.findMaxPursuitid();
                pursuitTracker.setPursuitid(maxPursuitid + 1);
            }

            PursuitTracker savedPursuitTracker = pursuitTrackerRepository.save(pursuitTracker);
            savedPursuitTrackers.add(savedPursuitTracker);
        }

        if (!duplicateProjectorPursuits.isEmpty()) {
            response.put("message", "The following entries with projectorPursuit already exist and cannot be saved! " + duplicateProjectorPursuits);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        response.put("message", "Data Saved Successfully!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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


    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePursuitTrackers(@Valid @RequestBody List<PursuitTracker> pursuitTrackerDetailsList) {
        Map<String, Object> response = new HashMap<>();
        List<PursuitTracker> updatedPursuitTrackers = new ArrayList<>();

        for (PursuitTracker pursuitTrackerDetails : pursuitTrackerDetailsList) {
            if (pursuitTrackerDetails.getProjectorPursuit() == null || pursuitTrackerDetails.getProjectorPursuit().isEmpty() ||
                    pursuitTrackerDetails.getPursuitstatus() == null || pursuitTrackerDetails.getPursuitstatus().isEmpty() ||
                    pursuitTrackerDetails.getType() == null || pursuitTrackerDetails.getType().isEmpty()) {
                response.put("message", "Required fields cannot be null or empty");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int pursuitTrackerId = pursuitTrackerDetails.getId();
            Optional<PursuitTracker> optionalPursuitTracker = pursuitTrackerRepository.findById(pursuitTrackerId);
            if (optionalPursuitTracker.isPresent()) {
                PursuitTracker existingPursuitTracker = optionalPursuitTracker.get();

                // Check if any fields have been changed
                Map<String, Object> changes = new HashMap<>();
                boolean updated = false;

                if (!Objects.equals(existingPursuitTracker.getDeliveryDirector(), pursuitTrackerDetails.getDeliveryDirector())) {
                    existingPursuitTracker.setDeliveryDirector(pursuitTrackerDetails.getDeliveryDirector());
                    changes.put("deliveryDirector", pursuitTrackerDetails.getDeliveryDirector());
                    updated = true;
                }
                if (!Objects.equals(existingPursuitTracker.getDeliveryManager(), pursuitTrackerDetails.getDeliveryManager())) {
                    existingPursuitTracker.setDeliveryManager(pursuitTrackerDetails.getDeliveryManager());
                    changes.put("deliveryManager", pursuitTrackerDetails.getDeliveryManager());
                    updated = true;
                }
                if (!Objects.equals(existingPursuitTracker.getAccount(), pursuitTrackerDetails.getAccount())) {
                    existingPursuitTracker.setAccount(pursuitTrackerDetails.getAccount());
                    changes.put("account", pursuitTrackerDetails.getAccount());
                    updated = true;
                }
                if (!Objects.equals(existingPursuitTracker.getType(), pursuitTrackerDetails.getType())) {
                    existingPursuitTracker.setType(pursuitTrackerDetails.getType());
                    changes.put("type", pursuitTrackerDetails.getType());
                    updated = true;
                }
                if (!Objects.equals(existingPursuitTracker.getTcv(), pursuitTrackerDetails.getTcv())) {
                    existingPursuitTracker.setTcv(pursuitTrackerDetails.getTcv());
                    changes.put("tcv", pursuitTrackerDetails.getTcv());
                    updated = true;
                }
                if (!Objects.equals(existingPursuitTracker.getIdentifiedmonth(), pursuitTrackerDetails.getIdentifiedmonth())) {
                    existingPursuitTracker.setIdentifiedmonth(pursuitTrackerDetails.getIdentifiedmonth());
                    changes.put("identifiedmonth", pursuitTrackerDetails.getIdentifiedmonth());
                    updated = true;
                }

                if (!Objects.equals(existingPursuitTracker.getPursuitstatus(), pursuitTrackerDetails.getPursuitstatus())) {
                    existingPursuitTracker.setPursuitstatus(pursuitTrackerDetails.getPursuitstatus());
                    changes.put("pursuitstatus", pursuitTrackerDetails.getPursuitstatus());
                    updated = true;
                }
                if (!Objects.equals(existingPursuitTracker.getProjectorPursuit(), pursuitTrackerDetails.getProjectorPursuit())) {
                    existingPursuitTracker.setProjectorPursuit(pursuitTrackerDetails.getProjectorPursuit());
                    changes.put("projectorPursuit", pursuitTrackerDetails.getProjectorPursuit());
                    updated = true;
                }
                if (!Objects.equals(existingPursuitTracker.getPursuitorpotential(), pursuitTrackerDetails.getPursuitorpotential())) {
                    existingPursuitTracker.setPursuitorpotential(pursuitTrackerDetails.getPursuitorpotential());
                    changes.put("pursuitorpotential", pursuitTrackerDetails.getPursuitorpotential());
                    updated = true;
                }
                if (!Objects.equals(existingPursuitTracker.getLikelyClosureorActualClosure(), pursuitTrackerDetails.getLikelyClosureorActualClosure())) {
                    existingPursuitTracker.setLikelyClosureorActualClosure(pursuitTrackerDetails.getLikelyClosureorActualClosure());
                    changes.put("likelyClosureorActualClosure", pursuitTrackerDetails.getLikelyClosureorActualClosure());
                    updated = true;
                }

                if (!Objects.equals(existingPursuitTracker.getRemarks(), pursuitTrackerDetails.getRemarks())) {
                    existingPursuitTracker.setRemarks(pursuitTrackerDetails.getRemarks());
                    changes.put("remarks", pursuitTrackerDetails.getRemarks());
                    updated = true;
                }

                // Calculate and update stage and pursuitProbability
                String stage = calculateStage(pursuitTrackerDetails.getPursuitstatus(), pursuitTrackerDetails.getType());
                existingPursuitTracker.setStage(stage);

                int pursuitProbability = calculatePursuitProbability(pursuitTrackerDetails.getPursuitstatus(), pursuitTrackerDetails.getType());
                existingPursuitTracker.setPursuitProbability(pursuitProbability);

                // Check for duplicate entry
                if (updated && !isDuplicateEntry(existingPursuitTracker)) {
                    // Save the updated PursuitTracker
                    PursuitTracker updatedPursuitTracker = pursuitTrackerRepository.save(existingPursuitTracker);
                    updatedPursuitTrackers.add(updatedPursuitTracker);
                    // Add changes to response
                    if (!changes.isEmpty()) {
                        response.put("message", "Data successfully updated.");
                        response.put("changes", changes);
                    }
                } else if (!updated) {
                    response.put("message", "No changes detected.");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    // if data entry with the given ID is a duplicate, add it to the response
                    response.put("message", "Duplicate entry: Data already exists.");
                    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
                }
            } else {
                // if data entry with the given ID is not found, add it to the response
                response.put("message", "Data entry not found for one or more entries.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }

        // response.put("data", updatedPursuitTrackers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private boolean isDuplicateEntry(PursuitTracker pursuitTracker) {
        // Retrieve all existing pursuit trackers excluding the current one being updated
        List<PursuitTracker> existingEntries = pursuitTrackerRepository.findAll();

        // Iterate through existing entries to check for duplicates
        for (PursuitTracker existingEntry : existingEntries) {
            // Exclude the current entry being updated from duplicate check
            if (existingEntry.getId() != (pursuitTracker.getId()) && areEntriesEqual(existingEntry, pursuitTracker)) {
                return true;
            }
        }
        return false;
    }

    private boolean areEntriesEqual(PursuitTracker entry1, PursuitTracker entry2) {
        // Compare all fields of the entries
        // Return true if all fields match, false otherwise
        return entry1.getDeliveryDirector().equals(entry2.getDeliveryDirector()) &&
                entry1.getDeliveryManager().equals(entry2.getDeliveryManager()) &&
                entry1.getAccount().equals(entry2.getAccount()) &&
                entry1.getType().equals(entry2.getType()) &&
                entry1.getTcv() == entry2.getTcv() &&
                entry1.getPursuitstatus().equals(entry2.getPursuitstatus()) &&
                entry1.getProjectorPursuit().equals(entry2.getProjectorPursuit()) &&
                entry1.getPursuitorpotential().equals(entry2.getPursuitorpotential()) &&
                entry1.getRemarks().equals(entry2.getRemarks()) &&
                entry1.getStage().equals(entry2.getStage()) &&
                entry1.getPursuitProbability() == entry2.getPursuitProbability();
    }


    @GetMapping("/getall")
    public List<PursuitTracker> getAllPursuitTrackers() {
        return pursuitTrackerRepository.findAll();
    }

    @GetMapping("/get")
    public List<PursuitTrackerDTO> getPursuitTrackers() {
        List<PursuitTracker> pursuitTrackers = pursuitTrackerRepository.findAll();
        return pursuitTrackers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    private PursuitTrackerDTO convertToDto(PursuitTracker pursuitTracker) {
        return new PursuitTrackerDTO(
                pursuitTracker.getId(),
                pursuitTracker.getDeliveryDirector(),
                pursuitTracker.getDeliveryManager(),
                pursuitTracker.getAccount(),
                pursuitTracker.getType(),
                pursuitTracker.getTcv(),
                pursuitTracker.getIdentifiedmonth(),
                pursuitTracker.getPursuitstatus(),
                pursuitTracker.getProjectorPursuit(),
                pursuitTracker.getPursuitorpotential(),
                pursuitTracker.getLikelyClosureorActualClosure(),
                pursuitTracker.getRemarks()
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deletePursuitTrackers(@RequestBody List<Integer> pursuitTrackerIds) {
        Map<String, Object> response = new HashMap<>();

        List<Integer> deletedIds = new ArrayList<>();
        List<Integer> notFoundIds = new ArrayList<>();

        for (int pursuitTrackerId : pursuitTrackerIds) {
            Optional<PursuitTracker> optionalPursuitTracker = pursuitTrackerRepository.findById(pursuitTrackerId);
            if (optionalPursuitTracker.isPresent()) {
                PursuitTracker pursuitTracker = optionalPursuitTracker.get();

                // Find and delete related PursuitActions first
                List<PursuitActions> pursuitActions = pursuitActionsRepository.findByPursuit(pursuitTracker.getProjectorPursuit());
                pursuitActionsRepository.deleteAll(pursuitActions);

                // Then delete the PursuitTracker
                pursuitTrackerRepository.delete(pursuitTracker);

                deletedIds.add(pursuitTrackerId);
            } else {
                notFoundIds.add(pursuitTrackerId);
            }
        }

        if (!notFoundIds.isEmpty()) {
            response.put("message", "Some entries were not found.");
            response.put("notFoundIds", notFoundIds);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("message", "Entries successfully deleted.");
        response.put("deletedIds", deletedIds);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }





}




