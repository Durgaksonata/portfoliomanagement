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
                errors.add("No matching Pursuits entry found for pursuit: " + pursuitAction.getPursuit());
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
    public ResponseEntity<String> updatePursuitActionsByActionItemNumber(@RequestBody List<PursuitActions> updatedPursuitActionsList) {
        for (PursuitActions updatedPursuitActions : updatedPursuitActionsList) {
            String actionItemNumber = updatedPursuitActions.getActionItemNumber();
            List<PursuitActions> existingPursuitActions = pursuitActionsService.findByActionItemNumber(actionItemNumber);

            if (existingPursuitActions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PursuitActions found with actionItemNumber: " + actionItemNumber);
            }

            for (PursuitActions pursuitAction : existingPursuitActions) {
                pursuitAction.setDeliveryDirector(updatedPursuitActions.getDeliveryDirector());
                pursuitAction.setDeliveryManager(updatedPursuitActions.getDeliveryManager());
                pursuitAction.setAccount(updatedPursuitActions.getAccount());
                pursuitAction.setPursuit(updatedPursuitActions.getPursuit());
                pursuitAction.setActionItemNumber(updatedPursuitActions.getActionItemNumber());
                pursuitAction.setActionDescription(updatedPursuitActions.getActionDescription());
                pursuitAction.setActionType(updatedPursuitActions.getActionType());
                pursuitAction.setStatus(updatedPursuitActions.getStatus());
                pursuitAction.setActionOwner(updatedPursuitActions.getActionOwner());
                pursuitAction.setDueDate(updatedPursuitActions.getDueDate());
                pursuitAction.setDependentActionItem(updatedPursuitActions.getDependentActionItem());
                pursuitAction.setRemarks(updatedPursuitActions.getRemarks());
                pursuitActionsService.save(pursuitAction);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("PursuitActions have been updated.");
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