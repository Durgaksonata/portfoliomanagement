package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_PursuitProbabilityRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitProbability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/MDPursuitProbability")
public class MD_PursuitProbabilityController {

    @Autowired
    private MD_PursuitProbabilityRepository MD_PursuitProbabilityrepo;

    // GET method to retrieve all MD_PursuitProbability entries
    @GetMapping("/get")
    public ResponseEntity<List<MD_PursuitProbability>> getAllPursuitProbabilities() {
        List<MD_PursuitProbability> pursuitProbabilities = MD_PursuitProbabilityrepo.findAll();
        return ResponseEntity.ok(pursuitProbabilities);
    }
    // POST method to add a new MD_PursuitProbability entry
    @PostMapping("/save")
    public ResponseEntity<MD_PursuitProbability> createPursuitProbability(@RequestBody MD_PursuitProbability pursuitProbability) {
        MD_PursuitProbability savedPursuitProbability = MD_PursuitProbabilityrepo.save(pursuitProbability);
        return ResponseEntity.ok(savedPursuitProbability);
    }

    @PutMapping("/update")
    public ResponseEntity<MD_PursuitProbability> updatePursuitProbability(@RequestBody MD_PursuitProbability pursuitProbability) {
        // Check if the ID exists in the database
        Optional<MD_PursuitProbability> existingPursuitProbability = MD_PursuitProbabilityrepo.findById(pursuitProbability.getId());
        if (existingPursuitProbability.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Update the existing entry with the provided details
        MD_PursuitProbability updatedPursuitProbability = MD_PursuitProbabilityrepo.save(pursuitProbability);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Updated successfully");
        return ResponseEntity.ok(updatedPursuitProbability);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePursuitProbabilities(@RequestBody List<Integer> ids) {
        // Convert list of IDs to a list of MD_PursuitProbability entities for validation
        List<MD_PursuitProbability> pursuitProbabilitiesToDelete = ids.stream()
                .map(id -> {
                    Optional<MD_PursuitProbability> optionalPursuitProbability = MD_PursuitProbabilityrepo.findById(id);
                    return optionalPursuitProbability.orElse(null); // If ID not found, return null
                })
                .filter(pp -> pp != null) // Filter out null elements (non-existent IDs)
                .collect(Collectors.toList());

        if (pursuitProbabilitiesToDelete.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MD_PursuitProbabilityrepo.deleteAll(pursuitProbabilitiesToDelete);
        return ResponseEntity.ok("Pursuit probabilities deleted successfully");
    }

}