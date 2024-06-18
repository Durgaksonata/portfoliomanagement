package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_PursuitStatusRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/mdpursuit")
public class MD_PursuitStatusController {

    @Autowired
    MD_PursuitStatusRepository pursuitRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_PursuitStatus>> getAllData() {
        List<MD_PursuitStatus> mdmonths = pursuitRepo.findAll();
        return ResponseEntity.ok(mdmonths);
    }

    @PostMapping("/save")
    public ResponseEntity<Object> createMdPursuitStatus(@RequestBody MD_PursuitStatus mdPursuitStatus) {
        // Check if a pursuit status with the same name already exists
        Optional<MD_PursuitStatus> existingPursuitStatus = pursuitRepo.findByPursuitStatus(mdPursuitStatus.getPursuitStatus());
        if (existingPursuitStatus.isPresent()) {
            // If a duplicate pursuit status exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: PursuitStatus '" + mdPursuitStatus.getPursuitStatus() + "' already exists.");
        }

        // Save the new pursuit status
        MD_PursuitStatus createdPursuitStatus = pursuitRepo.save(mdPursuitStatus);
        return new ResponseEntity<>(createdPursuitStatus, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdPursuitStatus(@RequestBody MD_PursuitStatus updatedPursuitStatus) {
        Optional<MD_PursuitStatus> existingPursuitStatus = pursuitRepo.findById(updatedPursuitStatus.getId());
        if (existingPursuitStatus.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Check for duplicate pursuit status names
        Optional<MD_PursuitStatus> duplicatePursuitStatus = pursuitRepo.findByPursuitStatus(updatedPursuitStatus.getPursuitStatus());
        if (duplicatePursuitStatus.isPresent() && duplicatePursuitStatus.get().getId() != updatedPursuitStatus.getId()) {
            // If a duplicate pursuit status exists and it's not the current pursuit status, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Duplicate entry: PursuitStatus '" + updatedPursuitStatus.getPursuitStatus() + "' already exists."));
        }

        // Update the existing entity with new data
        MD_PursuitStatus updatedEntity = existingPursuitStatus.get();
        updatedEntity.setPursuitStatus(updatedPursuitStatus.getPursuitStatus());

        MD_PursuitStatus savedEntity = pursuitRepo.save(updatedEntity);

        // Prepare the response with a success message and the updated entity
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Updated successfully");
        response.put("updatedPursuitStatus", savedEntity);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePursuitStatusByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = ids.stream()
                .filter(id -> {
                    Optional<MD_PursuitStatus> pursuitStatus = pursuitRepo.findById(id);
                    if (pursuitStatus.isEmpty()) {
                        return true;
                    }
                    pursuitRepo.deleteById(id);
                    return false;
                })
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No pursuit statuses found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Pursuit statuses with specified IDs have been deleted.");
    }

}