package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_VerticalRepository;
import com.sonata.portfoliomanagement.model.MD_Vertical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/mdvertical")
public class MD_VerticalController {

    @Autowired
    private MD_VerticalRepository verticalRepo;

    // Handles GET requests to retrieve all MD_Vertical records
    @GetMapping("/get")
    public ResponseEntity<?> getAllData() {
        List<MD_Vertical> mdVerticals = verticalRepo.findAll();
        if (!mdVerticals.isEmpty()) {
            return ResponseEntity.ok(mdVerticals);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No verticals found.");
        }
    }


    // Handles POST requests to create a new MD_Vertical record
    @PostMapping("/save")
    public ResponseEntity<?> createMdVertical(@RequestBody MD_Vertical mdVertical) {
        try {
            if (verticalRepo.existsByVertical(mdVertical.getVertical())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Duplicate entry for vertical: " + mdVertical.getVertical());
            }
            MD_Vertical createdVertical = verticalRepo.save(mdVertical);
            return new ResponseEntity<>("Vertical added successfully", HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving the vertical.");
        }
    }

    // Handles PUT requests to update an existing MD_Vertical record
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdVertical(@RequestBody MD_Vertical updatedVertical) {
        // Check if the vertical with the given ID exists
        Optional<MD_Vertical> existingVerticalOptional = verticalRepo.findById(updatedVertical.getId());

        if (!existingVerticalOptional.isPresent()) {
            // If vertical with the given ID is not found, return 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Check for duplicate vertical names
        Optional<MD_Vertical> duplicateVertical = verticalRepo.findByVertical(updatedVertical.getVertical());
        if (duplicateVertical.isPresent() && duplicateVertical.get().getId() != (updatedVertical.getId())) {
            // If a duplicate vertical exists and it's not the current vertical, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Duplicate entry: Vertical '" + updatedVertical.getVertical() + "' already exists."));
        }

        // Update the existing vertical with the new values
        MD_Vertical existingVertical = existingVerticalOptional.get();
        StringBuilder updateMessage = new StringBuilder("Updated successfully: ");

        // Example: Check and update the vertical name
        if (!existingVertical.getVertical().equals(updatedVertical.getVertical())) {
            updateMessage.append("Vertical name changed from '")
                    .append(existingVertical.getVertical())
                    .append("' to '")
                    .append(updatedVertical.getVertical())
                    .append("'. ");
            existingVertical.setVertical(updatedVertical.getVertical());
        }

        // Save the updated vertical
        MD_Vertical savedVertical = verticalRepo.save(existingVertical);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedVertical", savedVertical);

        // Return the updated vertical with 200 OK status
        return ResponseEntity.ok(response);
    }

    // Handles DELETE requests to delete multiple MD_Vertical records by IDs
    @DeleteMapping("/deleteMultiple")
    public ResponseEntity<String> deleteMultipleMdVerticals(@RequestBody List<Integer> verticalIds) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedVerticalNames = new ArrayList<>();

        for (Integer id : verticalIds) {
            Optional<MD_Vertical> verticalOptional = verticalRepo.findById(id);
            if (!verticalOptional.isPresent()) {
                notFoundIds.add(id);
            } else {
                MD_Vertical vertical = verticalOptional.get();
                deletedVerticalNames.add(vertical.getVertical());
                verticalRepo.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No verticals found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Verticals: " + deletedVerticalNames + "' deleted successfully.");
    }
}


