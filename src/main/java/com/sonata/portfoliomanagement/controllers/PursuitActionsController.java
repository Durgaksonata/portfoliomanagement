package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.PursuitActionsRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.model.PursuitActions;
import com.sonata.portfoliomanagement.model.PursuitActionsDTO;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import com.sonata.portfoliomanagement.services.PursuitActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/pursuitactions")
public class PursuitActionsController {
    @Autowired
    PursuitActionsRepository pursuitActionRepo;
    @Autowired
    PursuitTrackerRepository PursuitTrackerRepo;

    @Autowired
    PursuitActionsService pursuitActionsService;

    @GetMapping("/get")
    public ResponseEntity<List<PursuitActions>> getAllData() {
        List<PursuitActions> pursuitAction = pursuitActionRepo.findAll();
        return ResponseEntity.ok(pursuitAction);
    }

    @GetMapping("/getByPursuitId/{pursuitId}")
    public ResponseEntity<?> getPursuitActionsByPursuitId(@PathVariable int pursuitId) {
        List<PursuitTracker> pursuitTrackerList = PursuitTrackerRepo.findByPursuitid(pursuitId);

        if (pursuitTrackerList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PursuitActions found with pursuitId: " + pursuitId);
        }

        List<PursuitActionsDTO> result = pursuitTrackerList.stream()
                .map(action -> new PursuitActionsDTO(
                        action.getDeliveryManager(),
                        action.getDeliveryDirector(),
                        action.getAccount(),
                        action.getProjectorPursuit()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllPursuitIds")
    public ResponseEntity<?> getAllPursuitIds() {
        List<Integer> pursuitIds = PursuitTrackerRepo.findAll().stream()
                .map(PursuitTracker::getPursuitid)
                .collect(Collectors.toList());

        if (pursuitIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Pursuit IDs found.");
        }

        return ResponseEntity.ok(pursuitIds);
    }


    @PostMapping("/save")
    public ResponseEntity<?> createPursuitActions(@RequestBody List<PursuitActions> pursuitActionsList) {
        List<String> errors = new ArrayList<>();
        List<PursuitActions> createdPursuits = new ArrayList<>();

        for (PursuitActions pursuitAction : pursuitActionsList) {
            // Validate mandatory fields
            if (pursuitAction.getActionItemNumber() == null || pursuitAction.getActionItemNumber().trim().isEmpty()) {
                errors.add("ActionItemNumber cannot be empty for pursuit: " + pursuitAction.getPursuit());
                continue;
            }
            if (pursuitAction.getActionDescription() == null || pursuitAction.getActionDescription().trim().isEmpty()) {
                errors.add("ActionDescription cannot be empty for pursuit: " + pursuitAction.getPursuit());
                continue;
            }
            // Check if the pursuit exists in the PursuitTracker table
            PursuitTracker pursuitTracker = PursuitTrackerRepo.findByProjectorPursuit(pursuitAction.getPursuit())
                    .orElse(null);

            // If no matching PursuitTracker entry is found, add an error message and continue
            if (pursuitTracker == null) {
                errors.add("No matching Pursuits entry found in pursuit tracker: " + pursuitAction.getPursuit());
                continue;
            }

            // Check if the project_or_pursuit matches the pursuit field in the PursuitTracker entry
            if (!pursuitAction.getPursuit().equals(pursuitTracker.getProjectorPursuit())) {
                errors.add("project_or_pursuit does not match the pursuit field in PursuitTracker for pursuit: " + pursuitAction.getPursuit());
                continue;
            }

            // Generate the pursuitid for PursuitActions
            int pursuitid = pursuitTracker.getPursuitid();
            pursuitAction.setPursuitid(pursuitid);

            // Check for duplicate entry
            List<PursuitActions> existingEntries = pursuitActionRepo.findByDeliveryManagerAndDeliveryDirectorAndAccountAndPursuitAndActionItemNumberAndActionDescriptionAndActionTypeAndStatusAndActionOwnerAndDueDateAndDependentActionItemAndRemarks(
                    pursuitAction.getDeliveryManager(),
                    pursuitAction.getDeliveryDirector(),
                    pursuitAction.getAccount(),
                    pursuitAction.getPursuit(),
                    pursuitAction.getActionItemNumber(),
                    pursuitAction.getActionDescription(),
                    pursuitAction.getActionType(),
                    pursuitAction.getStatus(),
                    pursuitAction.getActionOwner(),
                    pursuitAction.getDueDate(),
                    pursuitAction.getDependentActionItem(),
                    pursuitAction.getRemarks()
            );

            if (!existingEntries.isEmpty()) {
                errors.add("Duplicate entry exists for pursuit: " + pursuitAction.getPursuit());
                continue;
            }

            // Save the pursuitAction object to the database
            PursuitActions createdPursuit = pursuitActionRepo.save(pursuitAction);
            createdPursuits.add(createdPursuit);
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Data saved successfully.");
       // response.put("data", createdPursuits);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePursuitActionsByActionItemNumber(@RequestBody List<PursuitActions> updatedPursuitActionsList) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> changesList = new ArrayList<>();
        List<PursuitActions> updatedPursuitActionsEntities = new ArrayList<>();

        for (PursuitActions updatedPursuitActions : updatedPursuitActionsList) {
            int id = updatedPursuitActions.getId();
            Optional<PursuitActions> existingPursuitActionsOptional = pursuitActionsService.findById(id);

            if (!existingPursuitActionsOptional.isPresent()) {
                response.put("message", "No PursuitActions found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Validate mandatory fields
            if (updatedPursuitActions.getActionItemNumber() == null || updatedPursuitActions.getActionItemNumber().trim().isEmpty()) {
                response.put("message", "ActionItemNumber cannot be empty for ID: " + id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (updatedPursuitActions.getActionDescription() == null || updatedPursuitActions.getActionDescription().trim().isEmpty()) {
                response.put("message", "ActionDescription cannot be empty for ID: " + id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            PursuitActions existingPursuitActions = existingPursuitActionsOptional.get();

            // Check if any fields have been changed
            Map<String, Object> changes = new HashMap<>();
            boolean updated = false;
            if (!Objects.equals(existingPursuitActions.getDeliveryDirector(), updatedPursuitActions.getDeliveryDirector())) {
                existingPursuitActions.setDeliveryDirector(updatedPursuitActions.getDeliveryDirector());
                changes.put("deliveryDirector", updatedPursuitActions.getDeliveryDirector());
                updated = true;
            }
            if (!Objects.equals(existingPursuitActions.getDeliveryManager(), updatedPursuitActions.getDeliveryManager())) {
                existingPursuitActions.setDeliveryManager(updatedPursuitActions.getDeliveryManager());
                changes.put("deliveryManager", updatedPursuitActions.getDeliveryManager());
                updated = true;
            }
            if (!Objects.equals(existingPursuitActions.getAccount(), updatedPursuitActions.getAccount())) {
                existingPursuitActions.setAccount(updatedPursuitActions.getAccount());
                changes.put("account", updatedPursuitActions.getAccount());
                updated = true;
            }
            if (!Objects.equals(existingPursuitActions.getPursuit(), updatedPursuitActions.getPursuit())) {
                existingPursuitActions.setPursuit(updatedPursuitActions.getPursuit());
                changes.put("pursuit", updatedPursuitActions.getPursuit());
                updated = true;
            }
            if (!Objects.equals(existingPursuitActions.getActionItemNumber(), updatedPursuitActions.getActionItemNumber())) {
                existingPursuitActions.setActionItemNumber(updatedPursuitActions.getActionItemNumber());
                changes.put("actionItemNumber", updatedPursuitActions.getActionItemNumber());
                updated = true;
            }

            if (!Objects.equals(existingPursuitActions.getActionDescription(), updatedPursuitActions.getActionDescription())) {
                existingPursuitActions.setActionDescription(updatedPursuitActions.getActionDescription());
                changes.put("actionDescription", updatedPursuitActions.getActionDescription());
                updated = true;
            }
            if (!Objects.equals(existingPursuitActions.getActionType(), updatedPursuitActions.getActionType())) {
                existingPursuitActions.setActionType(updatedPursuitActions.getActionType());
                changes.put("actionType", updatedPursuitActions.getActionType());
                updated = true;
            }
            if (!Objects.equals(existingPursuitActions.getStatus(), updatedPursuitActions.getStatus())) {
                existingPursuitActions.setStatus(updatedPursuitActions.getStatus());
                changes.put("status", updatedPursuitActions.getStatus());
                updated = true;
            }
            if (!Objects.equals(existingPursuitActions.getActionOwner(), updatedPursuitActions.getActionOwner())) {
                existingPursuitActions.setActionOwner(updatedPursuitActions.getActionOwner());
                changes.put("actionOwner", updatedPursuitActions.getActionOwner());
                updated = true;
            }

            if (!Objects.equals(existingPursuitActions.getDependentActionItem(), updatedPursuitActions.getDependentActionItem())) {
                existingPursuitActions.setDependentActionItem(updatedPursuitActions.getDependentActionItem());
                changes.put("dependentActionItem", updatedPursuitActions.getDependentActionItem());
                updated = true;
            }
            if (!Objects.equals(existingPursuitActions.getRemarks(), updatedPursuitActions.getRemarks())) {
                existingPursuitActions.setRemarks(updatedPursuitActions.getRemarks());
                changes.put("remarks", updatedPursuitActions.getRemarks());
                updated = true;
            }


            // Check for duplicate entry
            if (updated && !isDuplicateEntry(existingPursuitActions)) {
                // Save the updated PursuitActions
                PursuitActions updatedPursuitActionsEntity = pursuitActionsService.save(existingPursuitActions);
                updatedPursuitActionsEntities.add(updatedPursuitActionsEntity);
                // Add changes to the changes list
                changes.put("id", id);
                changesList.add(changes);
            } else if (!updated) {
                response.put("message", "No changes detected for ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // if data entry with the given ID is a duplicate, add it to the response
                response.put("message", "Duplicate entry: Data already exists for ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
        }

        response.put("message", "Data successfully updated.");
        response.put("changes", changesList);
       // response.put("data", updatedPursuitActionsEntities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean isDuplicateEntry(PursuitActions pursuitActions) {
        // Retrieve all existing pursuit actions excluding the current one being updated
        List<PursuitActions> existingEntries = pursuitActionsService.findAll();

        // Iterate through existing entries to check for duplicates
        for (PursuitActions existingEntry : existingEntries) {
            // Exclude the current entry being updated from duplicate check
            if (existingEntry.getId() != (pursuitActions.getId()) && areEntriesEqual(existingEntry, pursuitActions)) {
                return true;
            }
        }
        return false;
    }

    private boolean areEntriesEqual(PursuitActions entry1, PursuitActions entry2) {
        // Compare all fields of the entries
        // Return true if all fields match, false otherwise
        return Objects.equals(entry1.getDeliveryDirector(), entry2.getDeliveryDirector()) &&
                Objects.equals(entry1.getDeliveryManager(), entry2.getDeliveryManager()) &&
                Objects.equals(entry1.getAccount(), entry2.getAccount()) &&
                Objects.equals(entry1.getPursuit(), entry2.getPursuit()) &&
                Objects.equals(entry1.getActionDescription(), entry2.getActionDescription()) &&
                Objects.equals(entry1.getActionType(), entry2.getActionType()) &&
                Objects.equals(entry1.getStatus(), entry2.getStatus()) &&
                Objects.equals(entry1.getActionOwner(), entry2.getActionOwner()) &&
                Objects.equals(entry1.getDependentActionItem(), entry2.getDependentActionItem()) &&
                Objects.equals(entry1.getRemarks(), entry2.getRemarks());
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePursuitActionsByIds(@RequestBody List<Integer> ids) {
        for (int id : ids) {
            Optional<PursuitActions> pursuitActionOptional = pursuitActionsService.findById(id);

            if (!pursuitActionOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PursuitActions found with ID: " + id);
            }

            pursuitActionsService.delete(pursuitActionOptional.get());
        }

        return ResponseEntity.status(HttpStatus.OK).body("PursuitActions with specified IDs have been deleted.");
    }







}