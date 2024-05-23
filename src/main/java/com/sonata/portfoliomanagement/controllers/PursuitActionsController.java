package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.PursuitActionsRepository;
import com.sonata.portfoliomanagement.model.PursuitActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pursuitactions")
public class PursuitActionsController {
    @Autowired
    PursuitActionsRepository pursuitActionRepo;

    @GetMapping("/get")
    public ResponseEntity<List<PursuitActions>> getAllData() {
        List<PursuitActions> pursuitAction = pursuitActionRepo.findAll();
        return ResponseEntity.ok(pursuitAction);
    }
    @PostMapping("/save")
    public ResponseEntity<?> createPursuitAction(@RequestBody PursuitActions pursuitAction) {
        // Query the database to check for existing entry with the same values for all fields
        List<PursuitActions> existingEntries = pursuitActionRepo.findByDeliveryManagerAndAccountAndPursuitAndActionItemNumberAndActionDescriptionAndActionTypeAndStatusAndActionOwnerAndDueDateAndDependentActionItemAndRemarks(
                pursuitAction.getDeliveryManager(),
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

        // Check if there are any existing entries
        if (!existingEntries.isEmpty()) {
            // An entry with the same values for all fields already exists, don't save the data
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate entry exists.");
        }

        // Save the pursuitAction object to the database
        PursuitActions createdPursuit = pursuitActionRepo.save(pursuitAction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPursuit);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<PursuitActions> updatePursuitActions(@PathVariable int id, @RequestBody PursuitActions updatedPursuitActions) {
        // Check if PursuitAction with given id exists
        if (!pursuitActionRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Set the id of updatedPursuitAction
        updatedPursuitActions.setId(id);

        // Save the updated PursuitAction
        PursuitActions updatedActions = pursuitActionRepo.save(updatedPursuitActions);

        return ResponseEntity.ok(updatedActions);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePursuitAction(@PathVariable int id) {
        if (pursuitActionRepo.existsById(id)) {
            pursuitActionRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


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


}