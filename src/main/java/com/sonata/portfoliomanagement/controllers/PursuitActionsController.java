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
    public ResponseEntity<?> createPursuitAction(@RequestBody PursuitActions pursuitAction) {
        // Check if the pursuit exists in the PursuitTracker table
        PursuitTracker pursuitTracker = PursuitTrackerRepo.findByProjectorPursuit(pursuitAction.getPursuit())
                .orElse(null);

        // If no matching PursuitTracker entry is found, return an error response
        if (pursuitTracker == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No matching Pursuits entry found.");
        }

        // Check if the project_or_pursuit matches the pursuit field in the PursuitTracker entry
        if (!pursuitAction.getPursuit().equals(pursuitTracker.getProjectorPursuit())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("project_or_pursuit does not match the pursuit field in PursuitTracker.");
        }

//        // Generate the pursuitid for PursuitActions
//        int basePursuitid = pursuitTracker.getPursuitid();
//        long subtaskCount = pursuitActionRepo.countByPursuit(pursuitAction.getPursuit()) + 1;
//        String pursuitid = basePursuitid + "." + subtaskCount;
//        pursuitAction.setPursuitid(pursuitid);

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
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate entry exists.");
        }

        // Save the pursuitAction object to the database
        PursuitActions createdPursuit = pursuitActionRepo.save(pursuitAction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPursuit);
    }



//    @PutMapping("/update/{id}")
//    public ResponseEntity<PursuitActions> updatePursuitActions(@PathVariable int id, @RequestBody PursuitActions updatedPursuitActions) {
//        // Check if PursuitAction with given id exists
//        if (!pursuitActionRepo.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Set the id of updatedPursuitAction
//        updatedPursuitActions.setId(id);
//
//        // Save the updated PursuitAction
//        PursuitActions updatedActions = pursuitActionRepo.save(updatedPursuitActions);
//
//        return ResponseEntity.ok(updatedActions);
//    }


//this ive disabled temporarily
//    @DeleteMapping("/delete/{pursuitid}")
//    public ResponseEntity<String> deletePursuitActionsByPursuitid(@PathVariable String pursuitid) {
//        List<PursuitActions> pursuitActions = pursuitActionsService.findByPursuitid(pursuitid);
//
//        if (pursuitActions.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PursuitActions found with pursuitid: " + pursuitid);
//        }
//
//        pursuitActionsService.deleteByPursuitid(pursuitid);
//        return ResponseEntity.status(HttpStatus.OK).body("PursuitActions with pursuitid: " + pursuitid + " have been deleted.");
//    }




    @PostMapping("/databyDm")
    public ResponseEntity<List<Map<String, Object>>> getDataByFy(@RequestBody PursuitActions requestDTO) {
        String deliveryManager = requestDTO.getDeliveryManager();
        List<Map<String, Object>> data = new ArrayList<>();
        // Retrieve data based on delivery manager
        List<PursuitActions> dataList = (List<PursuitActions>) pursuitActionRepo.findByDeliveryManager(deliveryManager);
        System.out.println("Data Retrieved: " + dataList);

        // Extract data fields from the retrieved data
        for (PursuitActions dataItem : dataList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", dataItem.getId());
            dataMap.put("deliveryDirector", dataItem.getDeliveryDirector());
            dataMap.put("deliveryManager", dataItem.getDeliveryManager());
            dataMap.put("account", dataItem.getAccount());
            dataMap.put("pursuit", dataItem.getPursuit());
            dataMap.put("actionItemNumber", dataItem.getActionItemNumber());
            dataMap.put("actionDescription", dataItem.getActionDescription());
            dataMap.put("actionType", dataItem.getActionType());
            dataMap.put("status", dataItem.getStatus());
            dataMap.put("actionOwner", dataItem.getActionOwner());
            dataMap.put("dueDate", dataItem.getDueDate());
            dataMap.put("dependentActionItem", dataItem.getDependentActionItem());
            dataMap.put("remarks", dataItem.getRemarks());
            data.add(dataMap);
        }

        return ResponseEntity.ok(data);
    }

    @GetMapping("/getdmlist")
    public List<String> getdeliveryManagersList() {
        List<String> dmList = new ArrayList<>();
        List<PursuitActions> pursuitData = pursuitActionRepo.findAll();

        for (PursuitActions request : pursuitData) {
            dmList.add(request.getDeliveryManager());
        }
        return dmList.stream().distinct().collect(Collectors.toList());
    }


    @PutMapping("/update/{pursuitid}")
    public ResponseEntity<String> updatePursuitActionsByPursuitid(@PathVariable String pursuitid, @RequestBody PursuitActions updatedPursuitActions) {
        List<PursuitActions> existingPursuitActions = pursuitActionsService.findByPursuitid(Integer.parseInt(pursuitid));

        if (existingPursuitActions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PursuitActions found with pursuitid: " + pursuitid);
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

        return ResponseEntity.status(HttpStatus.OK).body("PursuitActions with pursuitid: " + pursuitid + " have been updated.");
    }
}