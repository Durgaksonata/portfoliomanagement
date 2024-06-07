package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.PursuitActionsRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.model.PursuitActions;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import com.sonata.portfoliomanagement.services.PursuitActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    @PostMapping("/save")
    public ResponseEntity<?> createPursuitActions(@RequestBody List<PursuitActions> pursuitActionsList) {
        List<String> errors = new ArrayList<>();
        List<PursuitActions> createdPursuits = new ArrayList<>();

        for (PursuitActions pursuitAction : pursuitActionsList) {
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

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPursuits);
    }






    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePursuitActionsByActionItemNumber(@RequestBody List<PursuitActions> updatedPursuitActionsList) {
        Map<String, Object> response = new HashMap<>();
        List<PursuitActions> updatedPursuitActionsEntities = new ArrayList<>();

        for (PursuitActions updatedPursuitActions : updatedPursuitActionsList) {
            String actionItemNumber = updatedPursuitActions.getActionItemNumber();
            List<PursuitActions> existingPursuitActionsList = pursuitActionsService.findByActionItemNumber(actionItemNumber);

            if (existingPursuitActionsList.isEmpty()) {
                response.put("message", "No PursuitActions found with actionItemNumber: " + actionItemNumber);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            for (PursuitActions existingPursuitActions : existingPursuitActionsList) {
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
            }
        }

        response.put("data", updatedPursuitActionsEntities);
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
        return entry1.getDeliveryDirector().equals(entry2.getDeliveryDirector()) &&
                entry1.getDeliveryManager().equals(entry2.getDeliveryManager()) &&
                entry1.getAccount().equals(entry2.getAccount()) &&
                entry1.getPursuit().equals(entry2.getPursuit()) &&
                entry1.getActionDescription().equals(entry2.getActionDescription()) &&
                entry1.getActionType().equals(entry2.getActionType()) &&
                entry1.getStatus().equals(entry2.getStatus()) &&
                entry1.getActionOwner().equals(entry2.getActionOwner()) &&
                entry1.getDependentActionItem().equals(entry2.getDependentActionItem()) &&
                entry1.getRemarks().equals(entry2.getRemarks());
    }



    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePursuitActionsByActionItemNumbers(@RequestBody List<String> actionItemNumbers) {
        for (String actionItemNumber : actionItemNumbers) {
            List<PursuitActions> existingPursuitActions = pursuitActionsService.findByActionItemNumber(actionItemNumber);

            if (existingPursuitActions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PursuitActions found with actionItemNumber: " + actionItemNumber);
            }

            for (PursuitActions pursuitAction : existingPursuitActions) {
                pursuitActionsService.delete(pursuitAction);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("PursuitActions with specified actionItemNumbers have been deleted.");
    }


}