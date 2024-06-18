package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_PursuitProbabilityRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitProbability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/mdpursuitprobability")
public class MD_PursuitProbabilityController {

    @Autowired
    private MD_PursuitProbabilityRepository pursuitProbabilityRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_PursuitProbability>> getAllPursuitProbabilities() {
        List<MD_PursuitProbability> pursuitProbabilities = pursuitProbabilityRepo.findAll();
        if (!pursuitProbabilities.isEmpty()) {
            return ResponseEntity.ok(pursuitProbabilities);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Object> createPursuitProbability(@RequestBody MD_PursuitProbability pursuitProbability) {
        if (pursuitProbability == null) {
            return ResponseEntity.badRequest().body("MD Pursuit Probability object cannot be null. Please provide valid data.");
        }

        if (pursuitProbability.getPursuitStatus() == null || pursuitProbability.getPursuitStatus().isEmpty() ||
                pursuitProbability.getType() == null || pursuitProbability.getType().isEmpty()) {
            return ResponseEntity.badRequest().body("Pursuit Status and Type cannot be null or empty. Please provide valid data.");
        }

        Optional<MD_PursuitProbability> existingPursuitProbability = pursuitProbabilityRepo.findByPursuitStatusAndType(pursuitProbability.getPursuitStatus(), pursuitProbability.getType());
        if (existingPursuitProbability.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: Pursuit Status '" + pursuitProbability.getPursuitStatus() + "' and Type '" + pursuitProbability.getType() + "' already exists.");
        }

        MD_PursuitProbability createdPursuitProbability = pursuitProbabilityRepo.save(pursuitProbability);
        return new ResponseEntity<>(createdPursuitProbability, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePursuitProbability(@RequestBody MD_PursuitProbability updatedPursuitProbability) {
        if (updatedPursuitProbability.getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Updated Pursuit Probability ID cannot be null. Please provide valid data."));
        }

        Optional<MD_PursuitProbability> pursuitProbabilityOptional = pursuitProbabilityRepo.findById(updatedPursuitProbability.getId());
        if (!pursuitProbabilityOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (updatedPursuitProbability.getPursuitStatus() == null || updatedPursuitProbability.getPursuitStatus().isEmpty() ||
                updatedPursuitProbability.getType() == null || updatedPursuitProbability.getType().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Pursuit Status and Type cannot be null or empty. Please provide valid data."));
        }

        Optional<MD_PursuitProbability> duplicatePursuitProbability = pursuitProbabilityRepo.findByPursuitStatusAndType(updatedPursuitProbability.getPursuitStatus(), updatedPursuitProbability.getType());
        if (duplicatePursuitProbability.isPresent() && !duplicatePursuitProbability.get().getId().equals(updatedPursuitProbability.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Duplicate entry: Pursuit Status '" + updatedPursuitProbability.getPursuitStatus() + "' and Type '" + updatedPursuitProbability.getType() + "' already exists."));
        }

        MD_PursuitProbability existingPursuitProbability = pursuitProbabilityOptional.get();
        StringBuilder updateMessage = new StringBuilder("Updated successfully: ");

        if (!existingPursuitProbability.getPursuitStatus().equals(updatedPursuitProbability.getPursuitStatus())) {
            updateMessage.append("Pursuit Status changed from '")
                    .append(existingPursuitProbability.getPursuitStatus())
                    .append("' to '")
                    .append(updatedPursuitProbability.getPursuitStatus())
                    .append("'. ");
            existingPursuitProbability.setPursuitStatus(updatedPursuitProbability.getPursuitStatus());
        }

        if (!existingPursuitProbability.getType().equals(updatedPursuitProbability.getType())) {
            updateMessage.append("Type changed from '")
                    .append(existingPursuitProbability.getType())
                    .append("' to '")
                    .append(updatedPursuitProbability.getType())
                    .append("'. ");
            existingPursuitProbability.setType(updatedPursuitProbability.getType());
        }

        if (existingPursuitProbability.getProbability() != updatedPursuitProbability.getProbability()) {
            updateMessage.append("Probability changed from '")
                    .append(existingPursuitProbability.getProbability())
                    .append("' to '")
                    .append(updatedPursuitProbability.getProbability())
                    .append("'. ");
            existingPursuitProbability.setProbability(updatedPursuitProbability.getProbability());
        }

        if (!existingPursuitProbability.getStage().equals(updatedPursuitProbability.getStage())) {
            updateMessage.append("Stage changed from '")
                    .append(existingPursuitProbability.getStage())
                    .append("' to '")
                    .append(updatedPursuitProbability.getStage())
                    .append("'. ");
            existingPursuitProbability.setStage(updatedPursuitProbability.getStage());
        }

        MD_PursuitProbability updatedPursuitProbabilityEntity = pursuitProbabilityRepo.save(existingPursuitProbability);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedPursuitProbability", updatedPursuitProbabilityEntity);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePursuitProbabilitiesByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedPursuitProbabilityDetails = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_PursuitProbability> pursuitProbabilityOptional = pursuitProbabilityRepo.findById(id);
            if (!pursuitProbabilityOptional.isPresent()) {
                notFoundIds.add(id);
            } else {
                MD_PursuitProbability pursuitProbability = pursuitProbabilityOptional.get();
                deletedPursuitProbabilityDetails.add("ID: " + id + ", Pursuit Status: " + pursuitProbability.getPursuitStatus() + ", Type: " + pursuitProbability.getType());
                pursuitProbabilityRepo.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No pursuit probabilities found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Pursuit Probabilities " + deletedPursuitProbabilityDetails + " deleted successfully");
    }
}