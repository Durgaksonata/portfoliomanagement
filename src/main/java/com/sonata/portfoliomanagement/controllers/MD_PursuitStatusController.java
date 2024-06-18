package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_PursuitStatusRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/mdpursuit")
public class MD_PursuitStatusController {

    @Autowired
    private MD_PursuitStatusRepository pursuitRepo;

    @GetMapping("/get")
    public ResponseEntity<?> getAllData() {
        List<MD_PursuitStatus> mdPursuitStatuses = pursuitRepo.findAll();
        if (!mdPursuitStatuses.isEmpty()) {
            return ResponseEntity.ok(mdPursuitStatuses);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No pursuit statuses found.");
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Object> createMdPursuitStatus(@RequestBody MD_PursuitStatus mdPursuitStatus) {
        if (mdPursuitStatus == null) {
            return ResponseEntity.badRequest().body("MD Pursuit Status object cannot be null. Please provide valid data.");
        }

        // Check if pursuit status name is null or empty
        if (mdPursuitStatus.getPursuitStatus() == null || mdPursuitStatus.getPursuitStatus().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Pursuit Status name cannot be null or empty. Please provide a valid name.");
        }

        // Check if a pursuit status with the same name already exists
        Optional<MD_PursuitStatus> existingPursuitStatus = pursuitRepo.findByPursuitStatus(mdPursuitStatus.getPursuitStatus());
        if (existingPursuitStatus.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: Pursuit Status '" + mdPursuitStatus.getPursuitStatus() + "' already exists.");
        }

        // Save the new pursuit status
        MD_PursuitStatus createdPursuitStatus = pursuitRepo.save(mdPursuitStatus);
        if (createdPursuitStatus == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save pursuit status. Please try again later.");
        }

        String responseMessage = "Data added: Pursuit Status '" + createdPursuitStatus.getPursuitStatus() + "' added successfully.";
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdPursuitStatus(@RequestBody MD_PursuitStatus updatedPursuitStatus) {
        if (updatedPursuitStatus.getPursuitStatus() == null || updatedPursuitStatus.getPursuitStatus().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Updated pursuit status or its ID cannot be null. Please provide valid data."));
        }

        // Check if the pursuit status with the given ID exists
        Optional<MD_PursuitStatus> existingPursuitStatusOptional = pursuitRepo.findById(updatedPursuitStatus.getId());
        if (!existingPursuitStatusOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Check for duplicate pursuit status names if the updated pursuit status name is not null
        Optional<MD_PursuitStatus> duplicatePursuitStatus = pursuitRepo.findByPursuitStatus(updatedPursuitStatus.getPursuitStatus());
        if (duplicatePursuitStatus.isPresent() && duplicatePursuitStatus.get().getId() != updatedPursuitStatus.getId()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Duplicate entry: Pursuit Status '" + updatedPursuitStatus.getPursuitStatus() + "' already exists."));
        }

        // Update the existing pursuit status with the new values
        MD_PursuitStatus existingPursuitStatus = existingPursuitStatusOptional.get();
        StringBuilder updateMessage = new StringBuilder("Updated successfully: ");

        // Example: Check and update the pursuit status name
        if (!existingPursuitStatus.getPursuitStatus().equals(updatedPursuitStatus.getPursuitStatus())) {
            updateMessage.append("Pursuit Status name changed from '")
                    .append(existingPursuitStatus.getPursuitStatus())
                    .append("' to '")
                    .append(updatedPursuitStatus.getPursuitStatus())
                    .append("'. ");
            existingPursuitStatus.setPursuitStatus(updatedPursuitStatus.getPursuitStatus());
        }

        // Save the updated pursuit status
        MD_PursuitStatus updatedPursuitStatusEntity = pursuitRepo.save(existingPursuitStatus);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedPursuitStatus", updatedPursuitStatusEntity);

        // Return the updated pursuit status with 200 OK status
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePursuitStatusesByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedPursuitStatusNames = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_PursuitStatus> pursuitStatusOptional = pursuitRepo.findById(id);
            if (!pursuitStatusOptional.isPresent()) {
                notFoundIds.add(id);
            } else {
                MD_PursuitStatus pursuitStatus = pursuitStatusOptional.get();
                deletedPursuitStatusNames.add(pursuitStatus.getPursuitStatus());
                pursuitRepo.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No pursuit statuses found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Pursuit Statuses '" + deletedPursuitStatusNames + "' deleted successfully.");
    }
}